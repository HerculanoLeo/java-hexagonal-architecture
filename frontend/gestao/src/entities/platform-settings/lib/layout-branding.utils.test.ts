import {mapPlatformSettingsRecord} from '../api/platform-settings.repository';
import {getPlatformSettingsSeedData} from './platform-settings.defaults';
import {getDefaultLayoutBranding, mapLayoutBranding,} from './layout-branding.utils';

describe('mapLayoutBranding', () => {
	it('maps platform settings into layout branding DTO', () => {
		const settings = mapPlatformSettingsRecord(getPlatformSettingsSeedData());
		const branding = mapLayoutBranding(settings);

		expect(branding).toEqual({
			appTitle: settings.appTitle,
			projectName: settings.projectName,
			logoSrc: settings.logoSrc,
			logoAlt: settings.projectName,
			buttonColors: settings.buttonColors,
			themeColors: settings.themeColors,
		});
	});
});

describe('getDefaultLayoutBranding', () => {
	it('returns branding based on default platform settings', () => {
		const branding = getDefaultLayoutBranding();

		expect(branding.projectName).toBe('Starter');
		expect(branding.logoSrc).toBe('/logo192.png');
	});
});
