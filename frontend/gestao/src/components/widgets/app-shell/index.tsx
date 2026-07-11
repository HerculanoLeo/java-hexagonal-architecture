import {useEffect, useState} from 'react';

import {AppShellFrame} from './AppShellFrame';
import {AppShellMain} from './AppShellMain';
import {AppShellProvider} from './AppShellProvider';
import {signOutWithKeycloak} from '@shared/auth/auth-client.configuration';

import type {AppShellProps} from './app-shell.types';

export function AppShell({ children, onSignOut }: AppShellProps) {
	const [isSidebarOpen, setIsSidebarOpen] = useState(false);

	function signOut() {
		void signOutWithKeycloak();
	}

	function openSidebar() {
		blurActiveElement();
		setIsSidebarOpen(true);
	}

	function closeSidebar() {
		setIsSidebarOpen(false);
	}

	useEffect(() => {
		const mediaQuery = window.matchMedia('(min-width: 1024px)');

		function closeSidebarOnDesktop(event: MediaQueryListEvent) {
			if (event.matches) {
				setIsSidebarOpen(false);
			}
		}

		if (mediaQuery.matches) {
			setIsSidebarOpen(false);
		}

		mediaQuery.addEventListener('change', closeSidebarOnDesktop);

		return () => {
			mediaQuery.removeEventListener('change', closeSidebarOnDesktop);
		};
	}, []);

	return (
		<AppShellProvider>
			<AppShellFrame
				isSidebarOpen={isSidebarOpen}
				onCloseSidebar={closeSidebar}
				onOpenSidebar={openSidebar}
				onSignOut={onSignOut ?? (() => void signOut())}
			>
				<AppShellMain>{children}</AppShellMain>
			</AppShellFrame>
		</AppShellProvider>
	);
}

function blurActiveElement() {
	if (document.activeElement instanceof HTMLElement) {
		document.activeElement.blur();
	}
}

export type { AppShellProps };

export {
	AppShellProvider,
	useAppShell,
	useOptionalAppShell,
} from './AppShellProvider';
export { usePageTitle } from './use-page-title';
