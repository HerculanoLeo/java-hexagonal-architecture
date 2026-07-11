import {cn} from '@shared/ui/utils';

import {shellMutedForegroundClassName} from '@shared/ui/theme-colors.utils';

export const menuOverlaySurfaceClassName = 'bg-popover text-popover-foreground';

export const menuOverlayMutedTextClassName = 'text-muted-foreground';

export const menuTitleClassName = cn(
	'flex items-center gap-2 px-3 py-2 text-xs font-semibold uppercase tracking-wide',
	shellMutedForegroundClassName,
);

export const menuOverlayTitleClassName = cn(
	'flex items-center gap-2 px-3 py-2 text-xs font-semibold uppercase tracking-wide',
	menuOverlayMutedTextClassName,
);

export const menuTitleActiveClassName =
	'text-[var(--menu-title-active-foreground)]';

export const menuItemInteractiveClassName =
	'rounded-lg transition-colors hover:bg-[var(--menu-item-hover-bg)] hover:text-[var(--menu-item-active-foreground)]';

export const menuItemActiveClassName =
	'bg-[var(--menu-item-active-bg)] text-[var(--menu-item-active-foreground)]';

export const menuSubmenuInteractiveClassName =
	'rounded-lg transition-colors hover:bg-[var(--menu-submenu-hover-bg)] hover:text-[var(--menu-submenu-active-foreground)]';

export const menuSubmenuActiveClassName =
	'bg-[var(--menu-submenu-active-bg)] text-[var(--menu-submenu-active-foreground)]';

export const menuTriggerInteractiveClassName =
	'rounded-lg transition-colors hover:bg-[var(--menu-item-hover-bg)] hover:text-[var(--menu-item-active-foreground)] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 data-[state=open]:bg-[var(--menu-item-hover-bg)] data-[state=open]:text-[var(--menu-item-active-foreground)]';

export const menuTriggerActiveClassName =
	'bg-[var(--menu-item-active-bg)] text-[var(--menu-item-active-foreground)]';
