import {getRouteApi} from '@tanstack/react-router';
import type {CSSProperties, PropsWithChildren} from 'react';
import {useMemo, useSyncExternalStore} from 'react';

import type {LayoutConfigDto} from '@entities/platform-settings';
import {getBrandingCssVariables} from '@shared/ui/branding.utils';
import {getResolvedThemeSnapshot, subscribeToThemePreference,} from '@shared/ui/theme-preference.utils';

const rootRoute = getRouteApi('__root__');

export type LayoutBrandingValue = {
	branding: LayoutConfigDto['branding'];
	brandingStyle: CSSProperties;
	layoutConfig: LayoutConfigDto;
};

export function useLayoutBranding(): LayoutBrandingValue {
	const layoutConfig = rootRoute.useLoaderData();
	const resolvedTheme = useSyncExternalStore(
		subscribeToThemePreference,
		getResolvedThemeSnapshot,
		() => layoutConfig.resolvedTheme,
	);

	return useMemo(
		() => ({
			branding: layoutConfig.branding,
			brandingStyle: getBrandingCssVariables(
				layoutConfig.branding.buttonColors,
				layoutConfig.branding.themeColors,
				resolvedTheme,
			),
			layoutConfig,
		}),
		[layoutConfig, resolvedTheme],
	);
}

export function LayoutBrandingProvider({ children }: PropsWithChildren) {
	const { brandingStyle } = useLayoutBranding();

	return (
		<div className="contents" style={brandingStyle}>
			{children}
		</div>
	);
}
