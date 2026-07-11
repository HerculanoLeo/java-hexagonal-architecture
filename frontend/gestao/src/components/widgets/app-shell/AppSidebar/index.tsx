import {useLocation} from '@tanstack/react-router';
import {ChevronDown, X} from 'lucide-react';
import {useState} from 'react';

import {Button} from '@components/ui/button';
import {Drawer, DrawerContent, DrawerDescription, DrawerHeader, DrawerTitle,} from '@components/ui/drawer';
import {cn} from '@shared/ui/utils';
import {shellChromeClassName} from '@shared/ui/theme-colors.utils';

import {AppSidebarFooter} from '../AppSidebarFooter';
import {useAppShell} from '../AppShellProvider';
import {isMenuActive, isMenuItemActive} from '../app-menu-active';
import {getMenuIcon} from '../app-menu-icons';
import {
    menuItemActiveClassName,
    menuItemInteractiveClassName,
    menuOverlayMutedTextClassName,
    menuOverlaySurfaceClassName,
    menuOverlayTitleClassName,
    menuSubmenuActiveClassName,
    menuSubmenuInteractiveClassName,
    menuTitleActiveClassName,
} from '../app-menu.styles';

import type {MenuDto, MenuItemDto} from '@entities/menu';

type AppSidebarProps = {
	isEnabled?: boolean;
	isOpen?: boolean;
	onClose?: () => void;
	variant: 'drawer' | 'fixed';
};

export function AppSidebar({
	isEnabled = true,
	isOpen = false,
	onClose,
	variant,
}: AppSidebarProps) {
	const { menus } = useAppShell();
	const pathname = useLocation({
		select: (location) => location.pathname,
	});

	if (variant === 'fixed') {
		return (
			<aside
				className={cn(
					'hidden h-full w-72 shrink-0 flex-col border-r border-sidebar-border/60 shadow-sm lg:flex',
					shellChromeClassName,
					!isEnabled && 'lg:hidden',
				)}
				aria-hidden={!isEnabled}
				aria-label="Menu lateral"
			>
				<SidebarContent menus={menus} pathname={pathname} />
			</aside>
		);
	}

	return (
		<Drawer
			direction="left"
			open={isOpen}
			onOpenChange={(open) => {
				if (!open) {
					onClose?.();
				}
			}}
		>
			<DrawerContent
				className={cn(
					'flex h-full w-72 flex-col p-0 sm:max-w-72',
					shellChromeClassName,
				)}
			>
				<DrawerHeader className="sr-only">
					<DrawerTitle>Menu lateral</DrawerTitle>
					<DrawerDescription>Navegação principal do sistema</DrawerDescription>
				</DrawerHeader>
				<SidebarContent
					menus={menus}
					onNavigate={onClose}
					pathname={pathname}
					showCloseButton
				/>
			</DrawerContent>
		</Drawer>
	);
}

function SidebarContent({
	menus,
	onNavigate,
	pathname,
	showCloseButton = false,
}: {
	menus: MenuDto[];
	onNavigate?: () => void;
	pathname: string;
	showCloseButton?: boolean;
}) {
	return (
		<div className="flex h-full min-h-0 flex-col">
			<div className="flex h-20 shrink-0 items-center justify-between border-b border-sidebar-border px-4">
				<p className="text-sm font-semibold tracking-tight">Menu</p>
				{showCloseButton && (
					<Button
						type="button"
						variant="ghost"
						size="icon-sm"
						onClick={onNavigate}
						aria-label="Fechar menu"
					>
						<X className="size-4" />
					</Button>
				)}
			</div>

			<nav
				className={cn(
					'min-h-0 flex-1 overflow-y-auto p-3',
					menuOverlaySurfaceClassName,
				)}
				aria-label="Navegação principal"
			>
				<div className="space-y-1">
					{menus.map((menu) => (
						<SidebarMenu
							key={menu.id}
							menu={menu}
							onNavigate={onNavigate}
							pathname={pathname}
						/>
					))}
				</div>
			</nav>

			<AppSidebarFooter />
		</div>
	);
}

function SidebarMenu({
	menu,
	onNavigate,
	pathname,
}: {
	menu: MenuDto;
	onNavigate?: () => void;
	pathname: string;
}) {
	const Icon = getMenuIcon(menu.icon);
	const isActive = isMenuActive(menu, pathname);

	return (
		<div className="space-y-1">
			<div
				className={cn(
					menuOverlayTitleClassName,
					isActive && menuTitleActiveClassName,
				)}
			>
				{Icon && <Icon className="size-4" />}
				{menu.label}
			</div>
			<div className="space-y-1">
				{menu.items.map((item) => (
					<SidebarMenuItem
						key={item.id}
						item={item}
						onNavigate={onNavigate}
						pathname={pathname}
					/>
				))}
			</div>
		</div>
	);
}

function SidebarMenuItem({
	item,
	onNavigate,
	pathname,
	isSubmenu = false,
}: {
	item: MenuItemDto;
	onNavigate?: () => void;
	pathname: string;
	isSubmenu?: boolean;
}) {
	const [isOpen, setIsOpen] = useState(false);
	const Icon = getMenuIcon(item.icon);
	const hasChildren = Boolean(item.items?.length);
	const isActive = isMenuItemActive(item, pathname);
	const interactiveClassName = isSubmenu
		? menuSubmenuInteractiveClassName
		: menuItemInteractiveClassName;
	const activeClassName = isSubmenu
		? menuSubmenuActiveClassName
		: menuItemActiveClassName;

	if (hasChildren) {
		return (
			<div>
				<button
					type="button"
					className={cn(
						'flex w-full cursor-pointer items-start gap-3 px-3 py-2.5 text-left text-sm',
						interactiveClassName,
						isActive && activeClassName,
					)}
					onClick={() => setIsOpen((currentValue) => !currentValue)}
					aria-expanded={isOpen}
					aria-current={isActive ? 'page' : undefined}
				>
					{Icon && (
						<Icon
							className={cn(
								'mt-0.5 size-4 shrink-0',
								!isActive && menuOverlayMutedTextClassName,
							)}
						/>
					)}
					<span className="min-w-0 flex-1">
						<span className="block font-medium">{item.label}</span>
						{item.description && (
							<span
								className={cn(
									'line-clamp-2 text-xs',
									!isActive && menuOverlayMutedTextClassName,
								)}
							>
								{item.description}
							</span>
						)}
					</span>
					<ChevronDown
						className={cn(
							'mt-0.5 size-4 shrink-0 transition-transform duration-200',
							!isActive && menuOverlayMutedTextClassName,
							isOpen && 'rotate-180',
						)}
					/>
				</button>

				<div
					className={cn(
						'grid transition-[grid-template-rows] duration-200 ease-out',
						isOpen ? 'grid-rows-[1fr]' : 'grid-rows-[0fr]',
					)}
				>
					<div className="min-h-0 overflow-hidden">
						<div className="ml-4 mt-1 space-y-1 border-l border-sidebar-border pl-3">
							{item.items?.map((childItem) => (
								<SidebarMenuItem
									key={childItem.id}
									isSubmenu
									item={childItem}
									onNavigate={onNavigate}
									pathname={pathname}
								/>
							))}
						</div>
					</div>
				</div>
			</div>
		);
	}

	return (
		<a
			href={item.externalUrl ?? item.route ?? '#'}
			target={item.externalUrl ? '_blank' : undefined}
			rel={item.externalUrl ? 'noreferrer' : undefined}
			aria-current={isActive ? 'page' : undefined}
			className={cn(
				'flex items-start gap-3 px-3 py-2.5 text-sm',
				interactiveClassName,
				isActive && activeClassName,
			)}
			onClick={onNavigate}
		>
			{Icon && (
				<Icon
					className={cn(
						'mt-0.5 size-4 shrink-0',
						!isActive && menuOverlayMutedTextClassName,
					)}
				/>
			)}
			<span className="min-w-0">
				<span className="block font-medium">{item.label}</span>
				{item.description && (
					<span
						className={cn(
							'line-clamp-2 text-xs',
							!isActive && menuOverlayMutedTextClassName,
						)}
					>
						{item.description}
					</span>
				)}
			</span>
		</a>
	);
}
