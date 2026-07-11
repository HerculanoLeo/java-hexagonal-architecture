import {DEFAULT_BUTTON_COLORS, DEFAULT_THEME_COLORS,} from '@entities/platform-settings/lib/platform-settings.defaults';

import {getBrandingCssVariables} from './branding.utils';

describe('getBrandingCssVariables', () => {
	it('merges button and theme CSS variables', () => {
		const variables = getBrandingCssVariables(
			DEFAULT_BUTTON_COLORS,
			DEFAULT_THEME_COLORS,
			'dark',
		);

		expect(variables['--btn-save-bg']).toBe(DEFAULT_BUTTON_COLORS.saveBg);
		expect(variables['--primary']).toBe(DEFAULT_THEME_COLORS.primary.dark.bg);
		expect(variables['--menu-item-active-bg']).toBe(
			DEFAULT_THEME_COLORS.menuNavigation.dark.itemActiveBg,
		);
	});
});
