import {CircleCheckIcon, InfoIcon, Loader2Icon, OctagonXIcon, TriangleAlertIcon,} from 'lucide-react';
import type {CSSProperties} from 'react';
import {useSyncExternalStore} from 'react';
import type {ToasterProps} from 'sonner';
import {Toaster as Sonner} from 'sonner';

type SonnerTheme = NonNullable<ToasterProps['theme']>;

const Toaster = ({ theme, ...props }: ToasterProps) => {
	const resolvedTheme = useSyncExternalStore(
		subscribeToThemeChanges,
		getThemeSnapshot,
		getServerThemeSnapshot,
	);

	return (
		<Sonner
			theme={theme ?? resolvedTheme}
			className="toaster group"
			icons={{
				success: <CircleCheckIcon className="size-4" />,
				info: <InfoIcon className="size-4" />,
				warning: <TriangleAlertIcon className="size-4" />,
				error: <OctagonXIcon className="size-4" />,
				loading: <Loader2Icon className="size-4 animate-spin" />,
			}}
			style={
				{
					'--normal-bg': 'var(--popover)',
					'--normal-text': 'var(--popover-foreground)',
					'--normal-border': 'var(--border)',
					'--border-radius': 'var(--radius)',
				} as CSSProperties
			}
			{...props}
		/>
	);
};

function subscribeToThemeChanges(onStoreChange: () => void) {
	if (typeof window === 'undefined') {
		return () => {};
	}

	const observer = new MutationObserver(onStoreChange);

	observer.observe(document.documentElement, {
		attributeFilter: ['class', 'data-theme'],
		attributes: true,
	});

	return () => {
		observer.disconnect();
	};
}

function getThemeSnapshot(): SonnerTheme {
	if (typeof document === 'undefined') {
		return 'system';
	}

	return document.documentElement.classList.contains('dark') ? 'dark' : 'light';
}

function getServerThemeSnapshot(): SonnerTheme {
	return 'system';
}

export { Toaster };
