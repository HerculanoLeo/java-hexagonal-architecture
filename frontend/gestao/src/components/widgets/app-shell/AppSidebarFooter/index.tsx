import {shellMutedForegroundClassName} from '@shared/ui/theme-colors.utils';
import {cn} from '@shared/ui/utils';

import {useAppShell} from '../AppShellProvider';

export function AppSidebarFooter() {
	const { appVersion } = useAppShell();

	return (
		<div
			className={cn(
				'shrink-0 border-t border-sidebar-border/60 px-4 py-3 text-center text-xs',
				shellMutedForegroundClassName,
			)}
		>
			Versão {appVersion}
		</div>
	);
}
