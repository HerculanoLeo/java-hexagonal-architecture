/**
 * Scroll contract:
 * - Sidebar nav: own overflow-y-auto inside h-full column
 * - AppShellMain: flex-1 overflow-y-auto in the main column
 * - Footers: shrink-0, no scroll
 */
import type {PropsWithChildren} from 'react';

import {AppMenuBar} from '../AppMenuBar';
import {AppShellFooter} from '../AppShellFooter';
import {useAppShell} from '../AppShellProvider';
import {AppSidebar} from '../AppSidebar';
import {AppTopbar} from '../AppTopbar';

type AppShellFrameProps = PropsWithChildren<{
	isSidebarOpen: boolean;
	onCloseSidebar: () => void;
	onOpenSidebar: () => void;
	onSignOut?: () => void;
}>;

export function AppShellFrame({
	children,
	isSidebarOpen,
	onCloseSidebar,
	onOpenSidebar,
	onSignOut,
}: AppShellFrameProps) {
	const { hasFixedSidebar, showMenuBar } = useAppShell();

	return (
		<div className="flex h-svh overflow-hidden bg-muted/50 text-foreground">
			<AppSidebar variant="fixed" isEnabled={hasFixedSidebar} />
			<AppSidebar
				variant="drawer"
				isOpen={isSidebarOpen}
				onClose={onCloseSidebar}
			/>

			<div className="flex min-h-0 min-w-0 flex-1 flex-col overflow-hidden">
				<AppTopbar onOpenSidebar={onOpenSidebar} onSignOut={onSignOut} />

				{showMenuBar && (
					<div className="hidden shrink-0 lg:block">
						<AppMenuBar />
					</div>
				)}

				{children}
				<AppShellFooter />
			</div>
		</div>
	);
}
