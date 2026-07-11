import type {CSSProperties} from 'react';

import type {PlatformButtonColorsDto, PlatformThemeColorsDto,} from '@entities/platform-settings';
import type {ResolvedTheme} from '@shared/ui/theme-preference.utils';
import {getThemeCssVariables} from '@shared/ui/theme-colors.utils';

export function getBrandingCssVariables(
	buttonColors: PlatformButtonColorsDto,
	themeColors: PlatformThemeColorsDto,
	resolvedTheme: ResolvedTheme,
): CSSProperties {
	return {
		'--btn-activate-bg': buttonColors.activateBg,
		'--btn-activate-hover': buttonColors.activateHover,
		'--btn-back-bg': buttonColors.backBg,
		'--btn-back-foreground': buttonColors.backForeground,
		'--btn-back-hover': buttonColors.backHover,
		'--btn-delete-bg': buttonColors.deleteBg,
		'--btn-delete-hover': buttonColors.deleteHover,
		'--btn-save-bg': buttonColors.saveBg,
		'--btn-save-hover': buttonColors.saveHover,
		...getThemeCssVariables(themeColors, resolvedTheme),
	} as CSSProperties;
}
