import type PlatformSettingsDto from '../model/platform-settings.dto';
import type {PlatformSettingsUpdateRequest} from '../model/platform-settings.schema';
import {mapThemeColorsFromRecord} from '../model/platform-settings.schema';
import {
    getDefaultPlatformSettingsValues,
    PLATFORM_SETTINGS_ID,
    resolveLogoSrc,
} from '../lib/platform-settings.defaults';
import {platformSettingsCache} from '../lib/platform-settings.cache';

function isPlatformSettingsCacheValid(settings: PlatformSettingsDto) {
	return Boolean(settings.themeColors.menuNavigation);
}

export async function loadPlatformSettings(): Promise<PlatformSettingsDto> {
	const cached = platformSettingsCache.get();

	if (cached && !isPlatformSettingsCacheValid(cached)) {
		platformSettingsCache.clear();
	}

	const validCache = platformSettingsCache.get();

	if (validCache) {
		return validCache;
	}

	const settings = await fetchPlatformSettingsFromDatabase();
	platformSettingsCache.set(settings);

	return settings;
}

export function setPlatformSettingsCache(settings: PlatformSettingsDto) {
	platformSettingsCache.set(settings);
}

export function clearPlatformSettingsCache() {
	platformSettingsCache.clear();
}

export function toPlatformSettingsRecord(
	settings: PlatformSettingsUpdateRequest & { id: string },
) {
	return {
		appTitle: settings.appTitle,
		buttonActivateBg: settings.buttonActivateBg,
		buttonActivateHover: settings.buttonActivateHover,
		buttonBackBg: settings.buttonBackBg,
		buttonBackForeground: settings.buttonBackForeground,
		buttonBackHover: settings.buttonBackHover,
		buttonDeleteBg: settings.buttonDeleteBg,
		buttonDeleteHover: settings.buttonDeleteHover,
		buttonSaveBg: settings.buttonSaveBg,
		buttonSaveHover: settings.buttonSaveHover,
		id: settings.id,
		logoSource: settings.logoSource,
		logoUrl: settings.logoUrl,
		menuLayout: settings.menuLayout,
		primaryBgDark: settings.primaryBgDark,
		primaryBgLight: settings.primaryBgLight,
		primaryForegroundDark: settings.primaryForegroundDark,
		primaryForegroundLight: settings.primaryForegroundLight,
		projectName: settings.projectName,
		secondaryBgDark: settings.secondaryBgDark,
		secondaryBgLight: settings.secondaryBgLight,
		secondaryForegroundDark: settings.secondaryForegroundDark,
		secondaryForegroundLight: settings.secondaryForegroundLight,
		shellGradientFromDark: settings.shellGradientFromDark,
		shellGradientFromLight: settings.shellGradientFromLight,
		shellGradientToDark: settings.shellGradientToDark,
		shellGradientToLight: settings.shellGradientToLight,
		shellForegroundDark: settings.shellForegroundDark,
		shellForegroundLight: settings.shellForegroundLight,
		shellMutedForegroundDark: settings.shellMutedForegroundDark,
		shellMutedForegroundLight: settings.shellMutedForegroundLight,
		menuTitleActiveForegroundLight: settings.menuTitleActiveForegroundLight,
		menuItemHoverBgLight: settings.menuItemHoverBgLight,
		menuItemActiveBgLight: settings.menuItemActiveBgLight,
		menuItemActiveForegroundLight: settings.menuItemActiveForegroundLight,
		menuSubmenuHoverBgLight: settings.menuSubmenuHoverBgLight,
		menuSubmenuActiveBgLight: settings.menuSubmenuActiveBgLight,
		menuSubmenuActiveForegroundLight: settings.menuSubmenuActiveForegroundLight,
		menuTitleActiveForegroundDark: settings.menuTitleActiveForegroundDark,
		menuItemHoverBgDark: settings.menuItemHoverBgDark,
		menuItemActiveBgDark: settings.menuItemActiveBgDark,
		menuItemActiveForegroundDark: settings.menuItemActiveForegroundDark,
		menuSubmenuHoverBgDark: settings.menuSubmenuHoverBgDark,
		menuSubmenuActiveBgDark: settings.menuSubmenuActiveBgDark,
		menuSubmenuActiveForegroundDark: settings.menuSubmenuActiveForegroundDark,
	};
}

function normalizePlatformSettingsRecord(
	record: Partial<ReturnType<typeof toPlatformSettingsRecord>> &
		Pick<ReturnType<typeof toPlatformSettingsRecord>, 'id'>,
): ReturnType<typeof toPlatformSettingsRecord> {
	const defaults = getDefaultPlatformSettingsValues();

	return {
		...defaults,
		...record,
	};
}

function mapPlatformSettings(
	record: ReturnType<typeof toPlatformSettingsRecord> & {
		updatedAt?: Date;
	},
): PlatformSettingsDto {
	return {
		appTitle: record.appTitle,
		buttonColors: {
			activateBg: record.buttonActivateBg,
			activateHover: record.buttonActivateHover,
			backBg: record.buttonBackBg,
			backForeground: record.buttonBackForeground,
			backHover: record.buttonBackHover,
			deleteBg: record.buttonDeleteBg,
			deleteHover: record.buttonDeleteHover,
			saveBg: record.buttonSaveBg,
			saveHover: record.buttonSaveHover,
		},
		id: record.id,
		logoSource: record.logoSource,
		logoSrc: resolveLogoSrc(record),
		logoUrl: record.logoUrl,
		menuLayout: record.menuLayout,
		projectName: record.projectName,
		themeColors: mapThemeColorsFromRecord(record),
		updatedAt: (record.updatedAt ?? new Date()).toISOString(),
	};
}

async function fetchPlatformSettingsFromDatabase(): Promise<PlatformSettingsDto> {
	const defaults = toPlatformSettingsRecord(getDefaultPlatformSettingsValues());
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		return mapPlatformSettings(defaults);
	}

	const existing = await prismaClient.platformSettings.findUnique({
		where: { id: PLATFORM_SETTINGS_ID },
	});

	if (existing) {
		return mapPlatformSettings(normalizePlatformSettingsRecord(existing));
	}

	const created = await prismaClient.platformSettings.create({
		data: defaults,
	});

	return mapPlatformSettings(normalizePlatformSettingsRecord(created));
}

export function mapPlatformSettingsRecord(
	record: Partial<ReturnType<typeof toPlatformSettingsRecord>> &
		Pick<ReturnType<typeof toPlatformSettingsRecord>, 'id'> & {
			updatedAt?: Date;
		},
) {
	return mapPlatformSettings(normalizePlatformSettingsRecord(record));
}
