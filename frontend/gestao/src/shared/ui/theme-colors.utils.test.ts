import {DEFAULT_THEME_COLORS} from '@entities/platform-settings/lib/platform-settings.defaults';

import {getShellGradientPreviewStyle, getShellTextPreviewStyle, getThemeCssVariables,} from './theme-colors.utils';

describe('getThemeCssVariables', () => {
	it('maps theme colors to CSS variables for the resolved theme', () => {
		const variables = getThemeCssVariables(DEFAULT_THEME_COLORS, 'light');

		expect(variables['--primary']).toBe(DEFAULT_THEME_COLORS.primary.light.bg);
		expect(variables['--shell-gradient-from']).toBe(
			DEFAULT_THEME_COLORS.shellGradient.light.from,
		);
		expect(variables['--menu-item-hover-bg']).toBe(
			DEFAULT_THEME_COLORS.menuNavigation.light.itemHoverBg,
		);
	});
});

describe('getShellGradientPreviewStyle', () => {
	it('builds a linear gradient preview style', () => {
		expect(getShellGradientPreviewStyle('#111111', '#222222')).toEqual({
			backgroundImage: 'linear-gradient(135deg, #111111, #222222)',
		});
	});
});

describe('getShellTextPreviewStyle', () => {
	it('builds shell text preview variables', () => {
		expect(getShellTextPreviewStyle('#ffffff', '#cccccc')).toEqual({
			color: '#ffffff',
			'--shell-foreground': '#ffffff',
			'--shell-muted-foreground': '#cccccc',
		});
	});
});
