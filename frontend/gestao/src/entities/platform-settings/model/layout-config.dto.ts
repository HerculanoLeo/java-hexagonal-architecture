import type {CSSProperties} from 'react';

import type {MenuLayout, PlatformButtonColorsDto, PlatformThemeColorsDto,} from '../model/platform-settings.dto';
import type {ResolvedTheme, ThemePreference,} from '@shared/ui/theme-preference.utils';

export type LayoutBrandingDto = {
	projectName: string;
	appTitle: string;
	logoSrc: string;
	logoAlt: string;
	buttonColors: PlatformButtonColorsDto;
	themeColors: PlatformThemeColorsDto;
};

export type LayoutConfigDto = {
	appVersion: string;
	branding: LayoutBrandingDto;
	brandingStyle: CSSProperties;
	menuLayout: MenuLayout;
	resolvedTheme: ResolvedTheme;
	themePreference: ThemePreference;
};
