import type {LayoutBrandingDto} from '../model/layout-config.dto';
import type PlatformSettingsDto from '../model/platform-settings.dto';
import {mapPlatformSettingsRecord, toPlatformSettingsRecord,} from '../api/platform-settings.repository';
import {getDefaultPlatformSettingsValues} from './platform-settings.defaults';

export function mapLayoutBranding(
	settings: PlatformSettingsDto,
): LayoutBrandingDto {
	return {
		appTitle: settings.appTitle,
		projectName: settings.projectName,
		logoSrc: settings.logoSrc,
		logoAlt: settings.projectName,
		buttonColors: settings.buttonColors,
		themeColors: settings.themeColors,
	};
}

export function getDefaultLayoutBranding(): LayoutBrandingDto {
	return mapLayoutBranding(
		mapPlatformSettingsRecord(
			toPlatformSettingsRecord(getDefaultPlatformSettingsValues()),
		),
	);
}
