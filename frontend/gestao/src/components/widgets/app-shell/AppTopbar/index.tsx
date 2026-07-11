import {Link} from '@tanstack/react-router';
import {Menu} from 'lucide-react';

import {Button} from '@components/ui/button';
import {shellChromeClassName, shellMutedForegroundClassName,} from '@shared/ui/theme-colors.utils';

import {AppThemeSelector} from '../AppThemeSelector';
import {useAppShell} from '../AppShellProvider';
import {AppUserMenu} from '../AppUserMenu';

type AppTopbarProps = {
	onOpenSidebar?: () => void;
	onSignOut?: () => void;
};

export function AppTopbar({ onOpenSidebar, onSignOut }: AppTopbarProps) {
	const { branding } = useAppShell();

	return (
		<header
			className={`z-30 flex h-20 shrink-0 items-center border-b border-border/60 shadow-sm backdrop-blur ${shellChromeClassName}`}
		>
			<div className="flex h-full shrink-0 items-center justify-center border-r border-border px-3 lg:hidden">
				<Button
					type="button"
					variant="ghost"
					size="icon-sm"
					onClick={onOpenSidebar}
					aria-label="Abrir menu"
				>
					<Menu className="size-4" />
				</Button>
			</div>

			<div className="flex min-w-0 flex-1 items-center gap-5 px-4 sm:px-6">
				<Link
					to="/"
					className={`flex h-10 w-36 shrink-0 cursor-pointer items-center justify-center rounded-lg border border-dashed border-border/60 bg-black/10 text-xs font-medium transition-colors hover:bg-black/15 ${shellMutedForegroundClassName}`}
					aria-label="Ir para a página inicial"
				>
					{branding.logoSrc ? (
						<img
							src={branding.logoSrc}
							alt={branding.logoAlt}
							className="max-h-8 max-w-32 object-contain"
						/>
					) : (
						<span>Logo</span>
					)}
				</Link>
			</div>

			<div className="flex h-full shrink-0 items-center gap-2 border-l border-border px-4 sm:px-6">
				<AppThemeSelector />
				<AppUserMenu onSignOut={onSignOut} />
			</div>
		</header>
	);
}
