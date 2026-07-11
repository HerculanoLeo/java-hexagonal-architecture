import {env} from '@/env';

import type {
    LogoSource,
    MenuLayout,
    PlatformButtonColorsDto,
    PlatformThemeColorsDto,
} from '../model/platform-settings.dto';

export const PLATFORM_SETTINGS_ID = 'default';

export const DEFAULT_STATIC_LOGO_SRC = '/logo192.png';

export const DEFAULT_APP_TITLE = 'Starter';

export const DEFAULT_PROJECT_NAME = 'Starter';

export const DEFAULT_BUTTON_COLORS: PlatformButtonColorsDto = {
	activateBg: '#2563eb',
	activateHover: '#1d4ed8',
	backBg: '#f1f5f9',
	backForeground: '#1e3a8a',
	backHover: '#e2e8f0',
	deleteBg: '#dc2626',
	deleteHover: '#b91c1c',
	saveBg: '#059669',
	saveHover: '#047857',
};

export const DEFAULT_THEME_COLORS: PlatformThemeColorsDto = {
	primary: {
		dark: {
			bg: '#3b82f6',
			foreground: '#0f172a',
		},
		light: {
			bg: '#1e40af',
			foreground: '#ffffff',
		},
	},
	secondary: {
		dark: {
			bg: '#334155',
			foreground: '#f1f5f9',
		},
		light: {
			bg: '#eff6ff',
			foreground: '#1e3a8a',
		},
	},
	shellGradient: {
		dark: {
			from: '#1e3a8a',
			to: '#0f172a',
		},
		light: {
			from: '#2563eb',
			to: '#1e3a8a',
		},
	},
	shellText: {
		dark: {
			foreground: '#f8fafc',
			mutedForeground: '#94a3b8',
		},
		light: {
			foreground: '#ffffff',
			mutedForeground: '#bfdbfe',
		},
	},
	menuNavigation: {
		dark: {
			titleActiveForeground: '#93c5fd',
			itemHoverBg: '#1e293b',
			itemActiveBg: '#1e40af',
			itemActiveForeground: '#f8fafc',
			submenuHoverBg: '#334155',
			submenuActiveBg: '#1d4ed8',
			submenuActiveForeground: '#f8fafc',
		},
		light: {
			titleActiveForeground: '#1e40af',
			itemHoverBg: '#eff6ff',
			itemActiveBg: '#dbeafe',
			itemActiveForeground: '#1e3a8a',
			submenuHoverBg: '#f0f9ff',
			submenuActiveBg: '#dbeafe',
			submenuActiveForeground: '#1e3a8a',
		},
	},
};

export type PlatformSettingsSeedData = {
	id: string;
	projectName: string;
	appTitle: string;
	menuLayout: MenuLayout;
	logoSource: LogoSource;
	logoUrl: string | null;
	buttonSaveBg: string;
	buttonSaveHover: string;
	buttonActivateBg: string;
	buttonActivateHover: string;
	buttonBackBg: string;
	buttonBackHover: string;
	buttonBackForeground: string;
	buttonDeleteBg: string;
	buttonDeleteHover: string;
	shellGradientFromLight: string;
	shellGradientToLight: string;
	shellGradientFromDark: string;
	shellGradientToDark: string;
	primaryBgLight: string;
	primaryForegroundLight: string;
	secondaryBgLight: string;
	secondaryForegroundLight: string;
	primaryBgDark: string;
	primaryForegroundDark: string;
	secondaryBgDark: string;
	secondaryForegroundDark: string;
	shellForegroundLight: string;
	shellMutedForegroundLight: string;
	shellForegroundDark: string;
	shellMutedForegroundDark: string;
	menuTitleActiveForegroundLight: string;
	menuItemHoverBgLight: string;
	menuItemActiveBgLight: string;
	menuItemActiveForegroundLight: string;
	menuSubmenuHoverBgLight: string;
	menuSubmenuActiveBgLight: string;
	menuSubmenuActiveForegroundLight: string;
	menuTitleActiveForegroundDark: string;
	menuItemHoverBgDark: string;
	menuItemActiveBgDark: string;
	menuItemActiveForegroundDark: string;
	menuSubmenuHoverBgDark: string;
	menuSubmenuActiveBgDark: string;
	menuSubmenuActiveForegroundDark: string;
};

export function getDefaultAppTitle() {
	return env.VITE_APP_TITLE ?? getPlatformSettingsSeedData().appTitle;
}

export function getDefaultPlatformSettingsValues() {
	return getPlatformSettingsSeedData({
		appTitle: getDefaultAppTitle(),
	});
}

export function getPlatformSettingsSeedData(
	options: { appTitle?: string } = {},
): PlatformSettingsSeedData {
	return {
		appTitle: options.appTitle?.trim() || DEFAULT_APP_TITLE,
		buttonActivateBg: DEFAULT_BUTTON_COLORS.activateBg,
		buttonActivateHover: DEFAULT_BUTTON_COLORS.activateHover,
		buttonBackBg: DEFAULT_BUTTON_COLORS.backBg,
		buttonBackForeground: DEFAULT_BUTTON_COLORS.backForeground,
		buttonBackHover: DEFAULT_BUTTON_COLORS.backHover,
		buttonDeleteBg: DEFAULT_BUTTON_COLORS.deleteBg,
		buttonDeleteHover: DEFAULT_BUTTON_COLORS.deleteHover,
		buttonSaveBg: DEFAULT_BUTTON_COLORS.saveBg,
		buttonSaveHover: DEFAULT_BUTTON_COLORS.saveHover,
		id: PLATFORM_SETTINGS_ID,
		logoSource: 'static',
		logoUrl: null,
		menuLayout: 'hybrid',
		primaryBgDark: DEFAULT_THEME_COLORS.primary.dark.bg,
		primaryBgLight: DEFAULT_THEME_COLORS.primary.light.bg,
		primaryForegroundDark: DEFAULT_THEME_COLORS.primary.dark.foreground,
		primaryForegroundLight: DEFAULT_THEME_COLORS.primary.light.foreground,
		projectName: DEFAULT_PROJECT_NAME,
		secondaryBgDark: DEFAULT_THEME_COLORS.secondary.dark.bg,
		secondaryBgLight: DEFAULT_THEME_COLORS.secondary.light.bg,
		secondaryForegroundDark: DEFAULT_THEME_COLORS.secondary.dark.foreground,
		secondaryForegroundLight: DEFAULT_THEME_COLORS.secondary.light.foreground,
		shellForegroundDark: DEFAULT_THEME_COLORS.shellText.dark.foreground,
		shellForegroundLight: DEFAULT_THEME_COLORS.shellText.light.foreground,
		shellGradientFromDark: DEFAULT_THEME_COLORS.shellGradient.dark.from,
		shellGradientFromLight: DEFAULT_THEME_COLORS.shellGradient.light.from,
		shellGradientToDark: DEFAULT_THEME_COLORS.shellGradient.dark.to,
		shellGradientToLight: DEFAULT_THEME_COLORS.shellGradient.light.to,
		shellMutedForegroundDark:
			DEFAULT_THEME_COLORS.shellText.dark.mutedForeground,
		shellMutedForegroundLight:
			DEFAULT_THEME_COLORS.shellText.light.mutedForeground,
		menuTitleActiveForegroundLight:
			DEFAULT_THEME_COLORS.menuNavigation.light.titleActiveForeground,
		menuItemHoverBgLight: DEFAULT_THEME_COLORS.menuNavigation.light.itemHoverBg,
		menuItemActiveBgLight:
			DEFAULT_THEME_COLORS.menuNavigation.light.itemActiveBg,
		menuItemActiveForegroundLight:
			DEFAULT_THEME_COLORS.menuNavigation.light.itemActiveForeground,
		menuSubmenuHoverBgLight:
			DEFAULT_THEME_COLORS.menuNavigation.light.submenuHoverBg,
		menuSubmenuActiveBgLight:
			DEFAULT_THEME_COLORS.menuNavigation.light.submenuActiveBg,
		menuSubmenuActiveForegroundLight:
			DEFAULT_THEME_COLORS.menuNavigation.light.submenuActiveForeground,
		menuTitleActiveForegroundDark:
			DEFAULT_THEME_COLORS.menuNavigation.dark.titleActiveForeground,
		menuItemHoverBgDark: DEFAULT_THEME_COLORS.menuNavigation.dark.itemHoverBg,
		menuItemActiveBgDark: DEFAULT_THEME_COLORS.menuNavigation.dark.itemActiveBg,
		menuItemActiveForegroundDark:
			DEFAULT_THEME_COLORS.menuNavigation.dark.itemActiveForeground,
		menuSubmenuHoverBgDark:
			DEFAULT_THEME_COLORS.menuNavigation.dark.submenuHoverBg,
		menuSubmenuActiveBgDark:
			DEFAULT_THEME_COLORS.menuNavigation.dark.submenuActiveBg,
		menuSubmenuActiveForegroundDark:
			DEFAULT_THEME_COLORS.menuNavigation.dark.submenuActiveForeground,
	};
}

export function resolveLogoSrc(settings: {
	logoSource: LogoSource;
	logoUrl: string | null;
}) {
	if (settings.logoSource === 'external' && settings.logoUrl?.trim()) {
		return settings.logoUrl.trim();
	}

	return DEFAULT_STATIC_LOGO_SRC;
}

export type { LogoSource, MenuLayout };
