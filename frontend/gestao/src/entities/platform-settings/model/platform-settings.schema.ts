import {z} from 'zod';

import type PlatformSettingsDto from '../model/platform-settings.dto';
import type {PlatformThemeColorsDto} from './platform-settings.dto';

export const logoSourceSchema = z.enum(['static', 'external']);

export const menuLayoutSchema = z.enum(['hybrid', 'sidebar']);

export const hexColorSchema = z
	.string()
	.trim()
	.regex(/^#[0-9A-Fa-f]{6}$/, 'Informe uma cor válida no formato #RRGGBB.');

const themeColorFields = {
	shellGradientFromLight: hexColorSchema,
	shellGradientToLight: hexColorSchema,
	shellGradientFromDark: hexColorSchema,
	shellGradientToDark: hexColorSchema,
	primaryBgLight: hexColorSchema,
	primaryForegroundLight: hexColorSchema,
	secondaryBgLight: hexColorSchema,
	secondaryForegroundLight: hexColorSchema,
	primaryBgDark: hexColorSchema,
	primaryForegroundDark: hexColorSchema,
	secondaryBgDark: hexColorSchema,
	secondaryForegroundDark: hexColorSchema,
	shellForegroundLight: hexColorSchema,
	shellMutedForegroundLight: hexColorSchema,
	shellForegroundDark: hexColorSchema,
	shellMutedForegroundDark: hexColorSchema,
	menuTitleActiveForegroundLight: hexColorSchema,
	menuItemHoverBgLight: hexColorSchema,
	menuItemActiveBgLight: hexColorSchema,
	menuItemActiveForegroundLight: hexColorSchema,
	menuSubmenuHoverBgLight: hexColorSchema,
	menuSubmenuActiveBgLight: hexColorSchema,
	menuSubmenuActiveForegroundLight: hexColorSchema,
	menuTitleActiveForegroundDark: hexColorSchema,
	menuItemHoverBgDark: hexColorSchema,
	menuItemActiveBgDark: hexColorSchema,
	menuItemActiveForegroundDark: hexColorSchema,
	menuSubmenuHoverBgDark: hexColorSchema,
	menuSubmenuActiveBgDark: hexColorSchema,
	menuSubmenuActiveForegroundDark: hexColorSchema,
} as const;

export const platformSettingsFormSchema = z
	.object({
		projectName: z.string().trim().min(1, 'Informe o nome do projeto.'),
		appTitle: z.string().trim().min(1, 'Informe o título da aplicação.'),
		menuLayout: menuLayoutSchema,
		logoSource: logoSourceSchema,
		logoUrl: z.string().trim().optional(),
		buttonSaveBg: hexColorSchema,
		buttonSaveHover: hexColorSchema,
		buttonActivateBg: hexColorSchema,
		buttonActivateHover: hexColorSchema,
		buttonBackBg: hexColorSchema,
		buttonBackHover: hexColorSchema,
		buttonBackForeground: hexColorSchema,
		buttonDeleteBg: hexColorSchema,
		buttonDeleteHover: hexColorSchema,
		...themeColorFields,
	})
	.superRefine((values, context) => {
		if (values.logoSource === 'external' && !values.logoUrl?.trim()) {
			context.addIssue({
				code: 'custom',
				message: 'Informe a URL da logo.',
				path: ['logoUrl'],
			});
		}
	});

export type PlatformSettingsFormValues = z.infer<
	typeof platformSettingsFormSchema
>;

export type PlatformSettingsUpdateRequest = {
	projectName: string;
	appTitle: string;
	menuLayout: z.infer<typeof menuLayoutSchema>;
	logoSource: z.infer<typeof logoSourceSchema>;
	logoUrl: string | null;
	buttonSaveBg: string;
	buttonSaveHover: string;
	buttonActivateBg: string;
	buttonActivateHover: string;
	buttonBackBg: string;
	buttonBackHover: string;
	buttonBackForeground: string;
	buttonDeleteBg: string;
	buttonDeleteHover: string;
	shellGradientFromLight: string;
	shellGradientToLight: string;
	shellGradientFromDark: string;
	shellGradientToDark: string;
	primaryBgLight: string;
	primaryForegroundLight: string;
	secondaryBgLight: string;
	secondaryForegroundLight: string;
	primaryBgDark: string;
	primaryForegroundDark: string;
	secondaryBgDark: string;
	secondaryForegroundDark: string;
	shellForegroundLight: string;
	shellMutedForegroundLight: string;
	shellForegroundDark: string;
	shellMutedForegroundDark: string;
	menuTitleActiveForegroundLight: string;
	menuItemHoverBgLight: string;
	menuItemActiveBgLight: string;
	menuItemActiveForegroundLight: string;
	menuSubmenuHoverBgLight: string;
	menuSubmenuActiveBgLight: string;
	menuSubmenuActiveForegroundLight: string;
	menuTitleActiveForegroundDark: string;
	menuItemHoverBgDark: string;
	menuItemActiveBgDark: string;
	menuItemActiveForegroundDark: string;
	menuSubmenuHoverBgDark: string;
	menuSubmenuActiveBgDark: string;
	menuSubmenuActiveForegroundDark: string;
};

export function parsePlatformSettingsForm(
	values: PlatformSettingsFormValues,
): PlatformSettingsUpdateRequest {
	return {
		appTitle: values.appTitle.trim(),
		buttonActivateBg: values.buttonActivateBg,
		buttonActivateHover: values.buttonActivateHover,
		buttonBackBg: values.buttonBackBg,
		buttonBackForeground: values.buttonBackForeground,
		buttonBackHover: values.buttonBackHover,
		buttonDeleteBg: values.buttonDeleteBg,
		buttonDeleteHover: values.buttonDeleteHover,
		buttonSaveBg: values.buttonSaveBg,
		buttonSaveHover: values.buttonSaveHover,
		logoSource: values.logoSource,
		logoUrl:
			values.logoSource === 'external'
				? (values.logoUrl?.trim() ?? null)
				: null,
		menuLayout: values.menuLayout,
		primaryBgDark: values.primaryBgDark,
		primaryBgLight: values.primaryBgLight,
		primaryForegroundDark: values.primaryForegroundDark,
		primaryForegroundLight: values.primaryForegroundLight,
		projectName: values.projectName.trim(),
		secondaryBgDark: values.secondaryBgDark,
		secondaryBgLight: values.secondaryBgLight,
		secondaryForegroundDark: values.secondaryForegroundDark,
		secondaryForegroundLight: values.secondaryForegroundLight,
		shellGradientFromDark: values.shellGradientFromDark,
		shellGradientFromLight: values.shellGradientFromLight,
		shellGradientToDark: values.shellGradientToDark,
		shellGradientToLight: values.shellGradientToLight,
		shellForegroundDark: values.shellForegroundDark,
		shellForegroundLight: values.shellForegroundLight,
		shellMutedForegroundDark: values.shellMutedForegroundDark,
		shellMutedForegroundLight: values.shellMutedForegroundLight,
		menuTitleActiveForegroundLight: values.menuTitleActiveForegroundLight,
		menuItemHoverBgLight: values.menuItemHoverBgLight,
		menuItemActiveBgLight: values.menuItemActiveBgLight,
		menuItemActiveForegroundLight: values.menuItemActiveForegroundLight,
		menuSubmenuHoverBgLight: values.menuSubmenuHoverBgLight,
		menuSubmenuActiveBgLight: values.menuSubmenuActiveBgLight,
		menuSubmenuActiveForegroundLight: values.menuSubmenuActiveForegroundLight,
		menuTitleActiveForegroundDark: values.menuTitleActiveForegroundDark,
		menuItemHoverBgDark: values.menuItemHoverBgDark,
		menuItemActiveBgDark: values.menuItemActiveBgDark,
		menuItemActiveForegroundDark: values.menuItemActiveForegroundDark,
		menuSubmenuHoverBgDark: values.menuSubmenuHoverBgDark,
		menuSubmenuActiveBgDark: values.menuSubmenuActiveBgDark,
		menuSubmenuActiveForegroundDark: values.menuSubmenuActiveForegroundDark,
	};
}

export function toPlatformSettingsFormValues(
	settings: PlatformSettingsUpdateRequest & {
		logoSrc?: string;
	},
): PlatformSettingsFormValues {
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
		logoSource: settings.logoSource,
		logoUrl: settings.logoUrl ?? '',
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

export function platformSettingsDtoToFormValues(
	settings: PlatformSettingsDto,
): PlatformSettingsFormValues {
	return {
		appTitle: settings.appTitle,
		buttonActivateBg: settings.buttonColors.activateBg,
		buttonActivateHover: settings.buttonColors.activateHover,
		buttonBackBg: settings.buttonColors.backBg,
		buttonBackForeground: settings.buttonColors.backForeground,
		buttonBackHover: settings.buttonColors.backHover,
		buttonDeleteBg: settings.buttonColors.deleteBg,
		buttonDeleteHover: settings.buttonColors.deleteHover,
		buttonSaveBg: settings.buttonColors.saveBg,
		buttonSaveHover: settings.buttonColors.saveHover,
		logoSource: settings.logoSource,
		logoUrl: settings.logoUrl ?? '',
		menuLayout: settings.menuLayout,
		primaryBgDark: settings.themeColors.primary.dark.bg,
		primaryBgLight: settings.themeColors.primary.light.bg,
		primaryForegroundDark: settings.themeColors.primary.dark.foreground,
		primaryForegroundLight: settings.themeColors.primary.light.foreground,
		projectName: settings.projectName,
		secondaryBgDark: settings.themeColors.secondary.dark.bg,
		secondaryBgLight: settings.themeColors.secondary.light.bg,
		secondaryForegroundDark: settings.themeColors.secondary.dark.foreground,
		secondaryForegroundLight: settings.themeColors.secondary.light.foreground,
		shellGradientFromDark: settings.themeColors.shellGradient.dark.from,
		shellGradientFromLight: settings.themeColors.shellGradient.light.from,
		shellGradientToDark: settings.themeColors.shellGradient.dark.to,
		shellGradientToLight: settings.themeColors.shellGradient.light.to,
		shellForegroundDark: settings.themeColors.shellText.dark.foreground,
		shellForegroundLight: settings.themeColors.shellText.light.foreground,
		shellMutedForegroundDark:
			settings.themeColors.shellText.dark.mutedForeground,
		shellMutedForegroundLight:
			settings.themeColors.shellText.light.mutedForeground,
		menuTitleActiveForegroundLight:
			settings.themeColors.menuNavigation.light.titleActiveForeground,
		menuItemHoverBgLight: settings.themeColors.menuNavigation.light.itemHoverBg,
		menuItemActiveBgLight:
			settings.themeColors.menuNavigation.light.itemActiveBg,
		menuItemActiveForegroundLight:
			settings.themeColors.menuNavigation.light.itemActiveForeground,
		menuSubmenuHoverBgLight:
			settings.themeColors.menuNavigation.light.submenuHoverBg,
		menuSubmenuActiveBgLight:
			settings.themeColors.menuNavigation.light.submenuActiveBg,
		menuSubmenuActiveForegroundLight:
			settings.themeColors.menuNavigation.light.submenuActiveForeground,
		menuTitleActiveForegroundDark:
			settings.themeColors.menuNavigation.dark.titleActiveForeground,
		menuItemHoverBgDark: settings.themeColors.menuNavigation.dark.itemHoverBg,
		menuItemActiveBgDark: settings.themeColors.menuNavigation.dark.itemActiveBg,
		menuItemActiveForegroundDark:
			settings.themeColors.menuNavigation.dark.itemActiveForeground,
		menuSubmenuHoverBgDark:
			settings.themeColors.menuNavigation.dark.submenuHoverBg,
		menuSubmenuActiveBgDark:
			settings.themeColors.menuNavigation.dark.submenuActiveBg,
		menuSubmenuActiveForegroundDark:
			settings.themeColors.menuNavigation.dark.submenuActiveForeground,
	};
}

export function mapThemeColorsFromRecord(
	record: PlatformSettingsUpdateRequest,
): PlatformThemeColorsDto {
	return {
		primary: {
			dark: {
				bg: record.primaryBgDark,
				foreground: record.primaryForegroundDark,
			},
			light: {
				bg: record.primaryBgLight,
				foreground: record.primaryForegroundLight,
			},
		},
		secondary: {
			dark: {
				bg: record.secondaryBgDark,
				foreground: record.secondaryForegroundDark,
			},
			light: {
				bg: record.secondaryBgLight,
				foreground: record.secondaryForegroundLight,
			},
		},
		shellGradient: {
			dark: {
				from: record.shellGradientFromDark,
				to: record.shellGradientToDark,
			},
			light: {
				from: record.shellGradientFromLight,
				to: record.shellGradientToLight,
			},
		},
		shellText: {
			dark: {
				foreground: record.shellForegroundDark,
				mutedForeground: record.shellMutedForegroundDark,
			},
			light: {
				foreground: record.shellForegroundLight,
				mutedForeground: record.shellMutedForegroundLight,
			},
		},
		menuNavigation: {
			dark: {
				titleActiveForeground: record.menuTitleActiveForegroundDark,
				itemHoverBg: record.menuItemHoverBgDark,
				itemActiveBg: record.menuItemActiveBgDark,
				itemActiveForeground: record.menuItemActiveForegroundDark,
				submenuHoverBg: record.menuSubmenuHoverBgDark,
				submenuActiveBg: record.menuSubmenuActiveBgDark,
				submenuActiveForeground: record.menuSubmenuActiveForegroundDark,
			},
			light: {
				titleActiveForeground: record.menuTitleActiveForegroundLight,
				itemHoverBg: record.menuItemHoverBgLight,
				itemActiveBg: record.menuItemActiveBgLight,
				itemActiveForeground: record.menuItemActiveForegroundLight,
				submenuHoverBg: record.menuSubmenuHoverBgLight,
				submenuActiveBg: record.menuSubmenuActiveBgLight,
				submenuActiveForeground: record.menuSubmenuActiveForegroundLight,
			},
		},
	};
}
