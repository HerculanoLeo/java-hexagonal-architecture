import {createServerFn} from '@tanstack/react-start';

import {env} from '@/env';
import {getBrandingCssVariables} from '@shared/ui/branding.utils';
import {getResolvedThemeFromCookie, getThemePreferenceCookie,} from '@shared/ui/theme-preference.server';

import type {LayoutConfigDto} from '../model/layout-config.dto';
import {mapLayoutBranding} from '../lib/layout-branding.utils';

export const getLayoutConfig = createServerFn({ method: 'GET' }).handler(
	async (): Promise<LayoutConfigDto> => {
		const { loadPlatformSettings } =
			await import('./platform-settings.repository');

		const settings = await loadPlatformSettings();
		const branding = mapLayoutBranding(settings);
		const themePreference = getThemePreferenceCookie();
		const resolvedTheme = getResolvedThemeFromCookie();

		return {
			appVersion: env.VERSION,
			branding,
			brandingStyle: getBrandingCssVariables(
				branding.buttonColors,
				branding.themeColors,
				resolvedTheme,
			),
			menuLayout: settings.menuLayout,
			resolvedTheme,
			themePreference,
		};
	},
);
