import {useLocation} from '@tanstack/react-router';
import {ChevronDown} from 'lucide-react';

import type {ReactNode} from 'react';

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSub,
    DropdownMenuSubContent,
    DropdownMenuSubTrigger,
    DropdownMenuTrigger,
} from '@components/ui/dropdown-menu';
import {cn} from '@shared/ui/utils';
import {shellChromeClassName, shellMutedForegroundClassName,} from '@shared/ui/theme-colors.utils';

import {useAppShell} from '../AppShellProvider';
import {isMenuActive, isMenuItemActive} from '../app-menu-active';
import {getMenuIcon} from '../app-menu-icons';
import {
    menuOverlaySurfaceClassName,
    menuSubmenuActiveClassName,
    menuSubmenuInteractiveClassName,
    menuTriggerActiveClassName,
    menuTriggerInteractiveClassName,
} from '../app-menu.styles';

import type {MenuDto, MenuItemDto} from '@entities/menu';

export function AppMenuBar() {
	const { menus } = useAppShell();
	const pathname = useLocation({
		select: (location) => location.pathname,
	});

	return (
		<nav
			className={`flex h-12 items-center border-b border-border/60 px-4 shadow-sm backdrop-blur sm:px-6 ${shellChromeClassName}`}
			aria-label="Menu principal"
		>
			<div className="flex h-full items-center gap-1">
				{menus.map((menu) => (
					<AppMenu key={menu.id} menu={menu} pathname={pathname} />
				))}
			</div>
		</nav>
	);
}

function AppMenu({ menu, pathname }: { menu: MenuDto; pathname: string }) {
	const Icon = getMenuIcon(menu.icon);
	const isActive = isMenuActive(menu, pathname);

	return (
		<DropdownMenu>
			<DropdownMenuTrigger asChild>
				<button
					type="button"
					className={cn(
						'group inline-flex h-9 cursor-pointer items-center gap-2 px-3 text-sm font-medium',
						menuTriggerInteractiveClassName,
						isActive && menuTriggerActiveClassName,
					)}
					aria-current={isActive ? 'page' : undefined}
				>
					{Icon && (
						<Icon
							className={cn(
								'size-4',
								!isActive && shellMutedForegroundClassName,
							)}
						/>
					)}
					{menu.label}
					<ChevronDown
						className={cn(
							'size-4 transition-transform duration-200 group-data-[state=open]:rotate-180',
							!isActive && shellMutedForegroundClassName,
						)}
					/>
				</button>
			</DropdownMenuTrigger>

			<DropdownMenuContent
				align="start"
				sideOffset={8}
				collisionPadding={16}
				className={cn(
					'w-80 rounded-xl p-2 shadow-lg',
					menuOverlaySurfaceClassName,
				)}
			>
				{menu.items.map((item) => (
					<AppMenuItem key={item.id} item={item} pathname={pathname} />
				))}
			</DropdownMenuContent>
		</DropdownMenu>
	);
}

function AppMenuItem({
	item,
	pathname,
	isSubmenu = true,
}: {
	item: MenuItemDto;
	pathname: string;
	isSubmenu?: boolean;
}) {
	const hasChildren = Boolean(item.items?.length);
	const isActive = isMenuItemActive(item, pathname);

	if (hasChildren) {
		return (
			<DropdownMenuSub>
				<DropdownMenuSubTrigger
					className={cn(
						'rounded-lg p-3',
						menuSubmenuInteractiveClassName,
						isActive && menuSubmenuActiveClassName,
					)}
				>
					<MenuItemContent item={item} isActive={isActive} />
				</DropdownMenuSubTrigger>
				<DropdownMenuSubContent
					align="start"
					sideOffset={10}
					collisionPadding={16}
					className={cn(
						'w-80 rounded-xl p-2 shadow-lg',
						menuOverlaySurfaceClassName,
					)}
				>
					{item.items?.map((childItem) => (
						<AppMenuItem
							key={childItem.id}
							isSubmenu
							item={childItem}
							pathname={pathname}
						/>
					))}
				</DropdownMenuSubContent>
			</DropdownMenuSub>
		);
	}

	return (
		<DropdownMenuItem
			asChild
			className={cn(
				'rounded-lg p-0',
				isSubmenu && menuSubmenuInteractiveClassName,
				isActive && isSubmenu && menuSubmenuActiveClassName,
			)}
		>
			<MenuItemLink item={item} isActive={isActive} isSubmenu={isSubmenu}>
				<MenuItemContent item={item} isActive={isActive} />
			</MenuItemLink>
		</DropdownMenuItem>
	);
}

function MenuItemLink({
	children,
	item,
	isActive,
	isSubmenu,
}: {
	children: ReactNode;
	item: MenuItemDto;
	isActive: boolean;
	isSubmenu: boolean;
}) {
	const href = item.externalUrl ?? item.route ?? '#';
	const isExternal = Boolean(item.externalUrl);

	return (
		<a
			href={href}
			target={isExternal ? '_blank' : undefined}
			rel={isExternal ? 'noreferrer' : undefined}
			aria-current={isActive ? 'page' : undefined}
			className={cn(
				'flex w-full cursor-pointer rounded-lg p-3 outline-none transition-colors',
				isSubmenu && menuSubmenuInteractiveClassName,
				isActive && isSubmenu && menuSubmenuActiveClassName,
			)}
		>
			{children}
		</a>
	);
}

function MenuItemContent({
	item,
	isActive = false,
}: {
	item: MenuItemDto;
	isActive?: boolean;
}) {
	const Icon = getMenuIcon(item.icon);

	return (
		<span className="flex min-w-0 items-start gap-3">
			{Icon && (
				<span
					className={cn(
						'flex size-9 shrink-0 items-center justify-center rounded-lg border border-border bg-muted/60 text-muted-foreground',
						isActive &&
							'border-[var(--menu-submenu-active-foreground)]/20 bg-[var(--menu-submenu-active-bg)] text-[var(--menu-submenu-active-foreground)]',
					)}
				>
					<Icon className="size-4" />
				</span>
			)}
			<span className="min-w-0 space-y-1">
				<span
					className={cn(
						'block truncate text-sm font-semibold',
						isActive
							? 'text-[var(--menu-submenu-active-foreground)]'
							: 'text-foreground',
					)}
				>
					{item.label}
				</span>
				{item.description && (
					<span
						className={cn(
							'line-clamp-2 text-sm leading-snug',
							isActive
								? 'text-[var(--menu-submenu-active-foreground)]/80'
								: 'text-muted-foreground',
						)}
					>
						{item.description}
					</span>
				)}
			</span>
		</span>
	);
}
