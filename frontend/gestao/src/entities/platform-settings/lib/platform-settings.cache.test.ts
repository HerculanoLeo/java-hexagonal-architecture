import {mapPlatformSettingsRecord} from '../api/platform-settings.repository';
import {getPlatformSettingsSeedData} from './platform-settings.defaults';
import {platformSettingsCache} from './platform-settings.cache';

describe('platformSettingsCache', () => {
	afterEach(() => {
		platformSettingsCache.clear();
	});

	it('stores and retrieves platform settings', () => {
		const settings = mapPlatformSettingsRecord(getPlatformSettingsSeedData());

		platformSettingsCache.set(settings);

		expect(platformSettingsCache.get()).toEqual(settings);
	});
});
