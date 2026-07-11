import {DEFAULT_STATIC_LOGO_SRC, getPlatformSettingsSeedData, resolveLogoSrc,} from './platform-settings.defaults';

describe('getPlatformSettingsSeedData', () => {
	it('returns static logo defaults', () => {
		const seed = getPlatformSettingsSeedData();

		expect(seed.logoSource).toBe('static');
		expect(seed.logoUrl).toBeNull();
		expect(seed.menuLayout).toBe('hybrid');
	});

	it('uses a custom app title when provided', () => {
		const seed = getPlatformSettingsSeedData({ appTitle: '  Custom Title  ' });

		expect(seed.appTitle).toBe('Custom Title');
	});
});

describe('resolveLogoSrc', () => {
	it('returns the static logo path for static source', () => {
		expect(
			resolveLogoSrc({
				logoSource: 'static',
				logoUrl: null,
			}),
		).toBe(DEFAULT_STATIC_LOGO_SRC);
	});

	it('returns the trimmed external URL when source is external', () => {
		expect(
			resolveLogoSrc({
				logoSource: 'external',
				logoUrl: '  https://cdn.example.com/logo.png  ',
			}),
		).toBe('https://cdn.example.com/logo.png');
	});

	it('falls back to the static logo when external URL is missing', () => {
		expect(
			resolveLogoSrc({
				logoSource: 'external',
				logoUrl: '   ',
			}),
		).toBe(DEFAULT_STATIC_LOGO_SRC);
	});
});
