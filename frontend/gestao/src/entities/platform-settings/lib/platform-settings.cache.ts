import type PlatformSettingsDto from '../model/platform-settings.dto';
import type {CacheStore} from '@shared/cache/cache-store.type';
import {createInMemoryCacheStore} from '@shared/cache/cache-store.type';

export type PlatformSettingsCacheStore = CacheStore<PlatformSettingsDto>;

export const platformSettingsCache: PlatformSettingsCacheStore =
	createInMemoryCacheStore<PlatformSettingsDto>('platformSettingsCache');
