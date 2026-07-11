export type LogoSource = 'static' | 'external';

export type MenuLayout = 'hybrid' | 'sidebar';

export type PlatformThemeColorPairDto = {
	bg: string;
	foreground: string;
};

export type PlatformThemeGradientDto = {
	from: string;
	to: string;
};

export type PlatformMenuNavigationColorsDto = {
	titleActiveForeground: string;
	itemHoverBg: string;
	itemActiveBg: string;
	itemActiveForeground: string;
	submenuHoverBg: string;
	submenuActiveBg: string;
	submenuActiveForeground: string;
};

export type PlatformThemeColorsDto = {
	shellGradient: {
		light: PlatformThemeGradientDto;
		dark: PlatformThemeGradientDto;
	};
	shellText: {
		light: {
			foreground: string;
			mutedForeground: string;
		};
		dark: {
			foreground: string;
			mutedForeground: string;
		};
	};
	primary: {
		light: PlatformThemeColorPairDto;
		dark: PlatformThemeColorPairDto;
	};
	secondary: {
		light: PlatformThemeColorPairDto;
		dark: PlatformThemeColorPairDto;
	};
	menuNavigation: {
		light: PlatformMenuNavigationColorsDto;
		dark: PlatformMenuNavigationColorsDto;
	};
};

export type PlatformButtonColorsDto = {
	saveBg: string;
	saveHover: string;
	activateBg: string;
	activateHover: string;
	backBg: string;
	backHover: string;
	backForeground: string;
	deleteBg: string;
	deleteHover: string;
};

export default interface PlatformSettingsDto {
	id: string;
	projectName: string;
	appTitle: string;
	menuLayout: MenuLayout;
	logoSource: LogoSource;
	logoUrl: string | null;
	buttonColors: PlatformButtonColorsDto;
	themeColors: PlatformThemeColorsDto;
	logoSrc: string;
	updatedAt: string;
}
