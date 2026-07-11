import {getRouteApi} from '@tanstack/react-router';
import type {PropsWithChildren} from 'react';
import {createContext, useContext, useMemo} from 'react';

import type {UsuarioAutenticadoDto} from '@entities/auth';
import type {MenuDto} from '@entities/menu';
import {useLayoutBranding} from '@components/widgets/layout-branding';

import {getMenuLayoutFlags} from '../app-shell.layout';

type AppShellContextValue = {
	appVersion: string;
	branding: ReturnType<typeof useLayoutBranding>['branding'];
	hasFixedSidebar: boolean;
	layoutConfig: ReturnType<typeof useLayoutBranding>['layoutConfig'];
	menus: MenuDto[];
	showMenuBar: boolean;
	usuario: UsuarioAutenticadoDto;
};

const AppShellContext = createContext<AppShellContextValue | null>(null);

const authenticatedRoute = getRouteApi('/(authenticated)');

export function useAppShell() {
	const context = useContext(AppShellContext);

	if (!context) {
		throw new Error('useAppShell must be used within AppShellProvider.');
	}

	return context;
}

export function useOptionalAppShell() {
	return useContext(AppShellContext);
}

export function AppShellProvider({ children }: PropsWithChildren) {
	const { branding, layoutConfig } = useLayoutBranding();
	const { menus, usuario } = authenticatedRoute.useLoaderData();
	const { hasFixedSidebar, showMenuBar } = getMenuLayoutFlags(
		layoutConfig.menuLayout,
	);

	const value = useMemo<AppShellContextValue>(
		() => ({
			appVersion: layoutConfig.appVersion,
			branding,
			hasFixedSidebar,
			layoutConfig,
			menus,
			showMenuBar,
			usuario,
		}),
		[branding, hasFixedSidebar, layoutConfig, menus, showMenuBar, usuario],
	);

	return (
		<AppShellContext.Provider value={value}>
			{children}
		</AppShellContext.Provider>
	);
}
