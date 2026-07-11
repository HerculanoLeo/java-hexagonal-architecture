import type {CSSProperties} from 'react';

import type {PlatformThemeColorsDto} from '@entities/platform-settings';
import type {ResolvedTheme} from '@shared/ui/theme-preference.utils';
import {cn} from '@shared/ui/utils';

export const shellGradientClassName =
	'bg-[linear-gradient(135deg,var(--shell-gradient-from),var(--shell-gradient-to))]';

export const shellForegroundClassName = 'text-[var(--shell-foreground)]';

export const shellMutedForegroundClassName =
	'text-[var(--shell-muted-foreground)]';

export const shellChromeClassName = cn(
	shellGradientClassName,
	shellForegroundClassName,
);

export function getThemeCssVariables(
	themeColors: PlatformThemeColorsDto,
	resolvedTheme: ResolvedTheme,
): CSSProperties {
	const gradient = themeColors.shellGradient[resolvedTheme];
	const shellText = themeColors.shellText[resolvedTheme];
	const primary = themeColors.primary[resolvedTheme];
	const secondary = themeColors.secondary[resolvedTheme];

	const menuNavigation = themeColors.menuNavigation[resolvedTheme];

	return {
		'--primary': primary.bg,
		'--primary-foreground': primary.foreground,
		'--secondary': secondary.bg,
		'--secondary-foreground': secondary.foreground,
		'--shell-foreground': shellText.foreground,
		'--shell-gradient-from': gradient.from,
		'--shell-gradient-to': gradient.to,
		'--shell-muted-foreground': shellText.mutedForeground,
		'--sidebar-foreground': shellText.foreground,
		'--menu-title-active-foreground': menuNavigation.titleActiveForeground,
		'--menu-item-hover-bg': menuNavigation.itemHoverBg,
		'--menu-item-active-bg': menuNavigation.itemActiveBg,
		'--menu-item-active-foreground': menuNavigation.itemActiveForeground,
		'--menu-submenu-hover-bg': menuNavigation.submenuHoverBg,
		'--menu-submenu-active-bg': menuNavigation.submenuActiveBg,
		'--menu-submenu-active-foreground': menuNavigation.submenuActiveForeground,
	} as CSSProperties;
}

export function getShellGradientPreviewStyle(
	from: string,
	to: string,
): CSSProperties {
	return {
		backgroundImage: `linear-gradient(135deg, ${from}, ${to})`,
	};
}

export function getShellTextPreviewStyle(
	foreground: string,
	mutedForeground: string,
): CSSProperties {
	return {
		color: foreground,
		'--shell-foreground': foreground,
		'--shell-muted-foreground': mutedForeground,
	} as CSSProperties;
}
