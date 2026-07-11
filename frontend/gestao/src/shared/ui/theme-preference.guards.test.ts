import {isThemePreference} from './theme-preference.guards';

describe('isThemePreference', () => {
	it('accepts supported theme values', () => {
		expect(isThemePreference('light')).toBe(true);
		expect(isThemePreference('dark')).toBe(true);
		expect(isThemePreference('system')).toBe(true);
	});

	it('rejects unsupported values', () => {
		expect(isThemePreference('auto')).toBe(false);
		expect(isThemePreference(null)).toBe(false);
		expect(isThemePreference(undefined)).toBe(false);
	});
});
