export type { default as PlatformSettingsDto } from './model/platform-settings.dto';
export type {
	LayoutBrandingDto,
	LayoutConfigDto,
} from './model/layout-config.dto';
export type {
	LogoSource,
	MenuLayout,
	PlatformButtonColorsDto,
	PlatformMenuNavigationColorsDto,
	PlatformThemeColorsDto,
} from './model/platform-settings.dto';

export {
	logoSourceSchema,
	mapThemeColorsFromRecord,
	menuLayoutSchema,
	parsePlatformSettingsForm,
	platformSettingsDtoToFormValues,
	platformSettingsFormSchema,
	toPlatformSettingsFormValues,
} from './model/platform-settings.schema';
export type {
	PlatformSettingsFormValues,
	PlatformSettingsUpdateRequest,
} from './model/platform-settings.schema';

export {
	DEFAULT_APP_TITLE,
	DEFAULT_BUTTON_COLORS,
	DEFAULT_PROJECT_NAME,
	DEFAULT_STATIC_LOGO_SRC,
	DEFAULT_THEME_COLORS,
	getDefaultAppTitle,
	getDefaultPlatformSettingsValues,
	getPlatformSettingsSeedData,
	PLATFORM_SETTINGS_ID,
	resolveLogoSrc,
} from './lib/platform-settings.defaults';

export {
	getDefaultLayoutBranding,
	mapLayoutBranding,
} from './lib/layout-branding.utils';

export { getLayoutConfig } from './api/layout-config.server-fn';
export {
	getPlatformSettings,
	PLATFORM_SETTINGS_QUERY_KEY,
	updatePlatformSettings,
} from './api/platform-settings.server-fn';
export type { PlatformSettingsCacheStore } from './lib/platform-settings.cache';

export {
	loadPlatformSettings,
	mapPlatformSettingsRecord,
	clearPlatformSettingsCache,
	setPlatformSettingsCache,
	toPlatformSettingsRecord,
} from './api/platform-settings.repository';

export {
	fetchPlatformSettings,
	getConfiguracoesQueryKey,
	invalidatePlatformSettings,
	savePlatformSettings,
} from './api/platform-settings.queries';
