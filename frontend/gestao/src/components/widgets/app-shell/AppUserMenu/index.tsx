import {Link, useLocation} from '@tanstack/react-router';
import {LogOut, UserRound} from 'lucide-react';
import {useState} from 'react';

import {Button} from '@components/ui/button';
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@components/ui/dialog';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuPortal,
    DropdownMenuTrigger,
} from '@components/ui/dropdown-menu';
import {cn} from '@shared/ui/utils';
import {shellMutedForegroundClassName} from '@shared/ui/theme-colors.utils';

import {useAppShell} from '../AppShellProvider';
import {isRouteActive} from '../app-menu-active';

type AppUserMenuProps = {
	onSignOut?: () => void;
};

export function AppUserMenu({ onSignOut }: AppUserMenuProps) {
	const { usuario } = useAppShell();
	const pathname = useLocation({
		select: (location) => location.pathname,
	});
	const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
	const [isSignOutDialogOpen, setIsSignOutDialogOpen] = useState(false);
	const grupoNome = usuario.grupo?.nome;
	const userInitials = getInitials(usuario.nome);
	const isMeActive = isRouteActive('/me', pathname);

	function openSignOutDialog() {
		setIsUserMenuOpen(false);

		window.setTimeout(() => {
			blurActiveElement();
			setIsSignOutDialogOpen(true);
		}, 0);
	}

	return (
		<>
			<DropdownMenu open={isUserMenuOpen} onOpenChange={setIsUserMenuOpen}>
				<DropdownMenuTrigger asChild>
					<button
						type="button"
						className="flex cursor-pointer items-center gap-3 rounded-xl px-2 py-1.5 text-left transition-colors hover:bg-black/10 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
						aria-label="Abrir menu do usuário"
					>
						<div
							className={cn(
								'flex size-10 items-center justify-center rounded-full bg-black/10 text-sm font-semibold',
								shellMutedForegroundClassName,
							)}
						>
							{userInitials}
						</div>
						<div className="hidden leading-tight sm:block">
							<p className="text-sm font-semibold text-[var(--shell-foreground)]">
								{usuario.nome}
							</p>
							<p className={cn('text-xs', shellMutedForegroundClassName)}>
								{grupoNome}
							</p>
						</div>
					</button>
				</DropdownMenuTrigger>

				<DropdownMenuPortal>
					<DropdownMenuContent
						align="end"
						sideOffset={10}
						className="z-50 min-w-48 overflow-hidden rounded-xl border border-border bg-popover p-1 text-popover-foreground shadow-lg outline-none"
					>
						<DropdownMenuItem asChild>
							<Link
								to="/me"
								aria-current={isMeActive ? 'page' : undefined}
								className={cn(
									'flex cursor-pointer items-center gap-2 rounded-lg px-3 py-2 text-sm outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground',
									isMeActive && 'bg-accent text-accent-foreground',
								)}
							>
								<UserRound className="size-4" />
								Meu Perfil
							</Link>
						</DropdownMenuItem>

						<DropdownMenuItem
							onSelect={(event) => {
								event.preventDefault();
								openSignOutDialog();
							}}
							className="flex cursor-pointer items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium text-destructive outline-none transition-colors hover:bg-destructive/10 focus:bg-destructive/10"
						>
							<LogOut className="size-4" />
							Sair
						</DropdownMenuItem>
					</DropdownMenuContent>
				</DropdownMenuPortal>
			</DropdownMenu>

			<Dialog open={isSignOutDialogOpen} onOpenChange={setIsSignOutDialogOpen}>
				<DialogContent>
					<DialogHeader>
						<DialogTitle>Deseja sair?</DialogTitle>
						<DialogDescription>
							Ao confirmar, seu usuário vai sair da área logada do sistema.
						</DialogDescription>
					</DialogHeader>

					<DialogFooter>
						<DialogClose asChild>
							<Button
								type="button"
								variant="secondary"
								className="hover:bg-secondary/80"
							>
								Cancelar
							</Button>
						</DialogClose>
						<Button
							type="button"
							variant="destructive"
							className="hover:bg-destructive/90"
							onClick={() => {
								setIsSignOutDialogOpen(false);
								onSignOut?.();
							}}
						>
							Sair
						</Button>
					</DialogFooter>
				</DialogContent>
			</Dialog>
		</>
	);
}

function blurActiveElement() {
	if (document.activeElement instanceof HTMLElement) {
		document.activeElement.blur();
	}
}

function getInitials(value: string) {
	const words = value.trim().split(/\s+/);

	if (words.length === 1) {
		return words[0].slice(0, 2).toUpperCase();
	}

	return words
		.slice(0, 2)
		.map((word) => word[0])
		.join('')
		.toUpperCase();
}
