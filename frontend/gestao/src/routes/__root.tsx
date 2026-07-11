import {createRootRouteWithContext, HeadContent, Navigate, Outlet, Scripts,} from '@tanstack/react-router';
import {TanStackRouterDevtoolsPanel} from '@tanstack/react-router-devtools';
import {TanStackDevtools} from '@tanstack/react-devtools';

import {Toaster} from '@components/ui/sonner';
import TanStackQueryDevtools from '@configurations/tanstack-query/devtools';
import {LayoutBrandingProvider} from '@components/widgets/layout-branding';
import {themeCookieName, themeStorageKey,} from '@shared/ui/theme-preference.constants';

import appCss from '../styles.css?url';

import type {QueryClient} from '@tanstack/react-query';
import type {PropsWithChildren} from 'react';
import {useSyncExternalStore} from 'react';
import {DEFAULT_APP_TITLE, getLayoutConfig,} from '@entities/platform-settings';
import {getResolvedThemeSnapshot, subscribeToThemePreference,} from '@shared/ui/theme-preference.utils';
import {cn} from '@shared/ui/utils';

interface MyRouterContext {
	queryClient: QueryClient;
}

const themeInitScript = `
(() => {
  const storageKey = ${JSON.stringify(themeStorageKey)};
  const cookieName = ${JSON.stringify(themeCookieName)};
  const cookieMatch = document.cookie.match(new RegExp('(?:^|; )' + cookieName + '=([^;]*)'));
  const cookieTheme = cookieMatch ? decodeURIComponent(cookieMatch[1]) : null;
  const storedTheme = localStorage.getItem(storageKey);
  const theme = ['light', 'dark', 'system'].includes(cookieTheme ?? '')
    ? cookieTheme
    : ['light', 'dark', 'system'].includes(storedTheme ?? '')
      ? storedTheme
      : 'system';

  localStorage.setItem(storageKey, theme);

  if (!cookieTheme && theme) {
    document.cookie = cookieName + '=' + encodeURIComponent(theme) + '; path=/; max-age=31536000; samesite=lax';
  }

  if (!document.documentElement.dataset.theme) {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const shouldUseDarkTheme = theme === 'dark' || (theme === 'system' && prefersDark);

    document.documentElement.classList.toggle('dark', shouldUseDarkTheme);
    document.documentElement.dataset.theme = theme;
  }
})();
`;

export const Route = createRootRouteWithContext<MyRouterContext>()({
	head: () => ({
		meta: [
			{
				charSet: 'utf-8',
			},
			{
				name: 'viewport',
				content: 'width=device-width, initial-scale=1',
			},
			{
				title: DEFAULT_APP_TITLE,
			},
		],
		links: [
			{
				rel: 'stylesheet',
				href: appCss,
			},
		],
	}),
	loader: () => getLayoutConfig(),
	shellComponent: RootDocument,
	component: RootComponent,
	errorComponent: () => <Navigate to="/error" />,
});

function RootComponent() {
	return (
		<LayoutBrandingProvider>
			<Outlet />
		</LayoutBrandingProvider>
	);
}

function RootDocument({ children }: PropsWithChildren) {
	const { resolvedTheme, themePreference } = Route.useLoaderData();

	return (
		<html
			lang="pt-Br"
			className={cn(resolvedTheme === 'dark' && 'dark')}
			data-theme={themePreference}
			suppressHydrationWarning
		>
			<head>
				<script dangerouslySetInnerHTML={{ __html: themeInitScript }} />
				<HeadContent />
			</head>
			<body>
				{children}
				<RootDocumentToaster />
				<TanStackDevtools
					config={{
						position: 'bottom-left',
					}}
					plugins={[
						{
							name: 'Tanstack Router',
							render: <TanStackRouterDevtoolsPanel />,
						},
						TanStackQueryDevtools,
					]}
				/>
				<Scripts />
			</body>
		</html>
	);
}

function RootDocumentToaster() {
	const { resolvedTheme: serverResolvedTheme } = Route.useLoaderData();
	const resolvedTheme = useSyncExternalStore(
		subscribeToThemePreference,
		getResolvedThemeSnapshot,
		() => serverResolvedTheme,
	);

	return (
		<Toaster
			richColors
			position="top-right"
			closeButton
			theme={resolvedTheme}
		/>
	);
}
