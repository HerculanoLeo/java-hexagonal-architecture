import {mapPlatformSettingsRecord} from '../api/platform-settings.repository';
import {getPlatformSettingsSeedData} from '../lib/platform-settings.defaults';
import {
    hexColorSchema,
    mapThemeColorsFromRecord,
    parsePlatformSettingsForm,
    platformSettingsDtoToFormValues,
    platformSettingsFormSchema,
    type PlatformSettingsFormValues,
    toPlatformSettingsFormValues,
} from './platform-settings.schema';

function createValidFormValues(
	overrides: Partial<PlatformSettingsFormValues> = {},
): PlatformSettingsFormValues {
	return {
		...toPlatformSettingsFormValues(getPlatformSettingsSeedData()),
		...overrides,
	};
}

describe('hexColorSchema', () => {
	it('accepts a valid hex color', () => {
		expect(hexColorSchema.safeParse('#1e40af').success).toBe(true);
	});

	it('rejects invalid hex colors', () => {
		expect(hexColorSchema.safeParse('1e40af').success).toBe(false);
		expect(hexColorSchema.safeParse('#fff').success).toBe(false);
		expect(hexColorSchema.safeParse('not-a-color').success).toBe(false);
	});
});

describe('platformSettingsFormSchema', () => {
	it('accepts valid form values', () => {
		const result = platformSettingsFormSchema.safeParse(createValidFormValues());

		expect(result.success).toBe(true);
	});

	it('rejects empty project and app titles', () => {
		const result = platformSettingsFormSchema.safeParse(
			createValidFormValues({
				projectName: '   ',
				appTitle: '',
			}),
		);

		expect(result.success).toBe(false);
	});

	it('requires logoUrl when logoSource is external', () => {
		const result = platformSettingsFormSchema.safeParse(
			createValidFormValues({
				logoSource: 'external',
				logoUrl: '   ',
			}),
		);

		expect(result.success).toBe(false);

		if (!result.success) {
			expect(result.error.issues.some((issue) => issue.path[0] === 'logoUrl')).toBe(
				true,
			);
		}
	});

	it('accepts external logo when logoUrl is provided', () => {
		const result = platformSettingsFormSchema.safeParse(
			createValidFormValues({
				logoSource: 'external',
				logoUrl: 'https://cdn.example.com/logo.png',
			}),
		);

		expect(result.success).toBe(true);
	});
});

describe('parsePlatformSettingsForm', () => {
	it('trims textual fields and nullifies logoUrl for static logos', () => {
		const parsed = parsePlatformSettingsForm(
			createValidFormValues({
				projectName: '  Meu Projeto  ',
				appTitle: '  Meu App  ',
				logoSource: 'static',
				logoUrl: 'https://should-be-ignored.example/logo.png',
			}),
		);

		expect(parsed.projectName).toBe('Meu Projeto');
		expect(parsed.appTitle).toBe('Meu App');
		expect(parsed.logoUrl).toBeNull();
	});

	it('trims external logoUrl', () => {
		const parsed = parsePlatformSettingsForm(
			createValidFormValues({
				logoSource: 'external',
				logoUrl: '  https://cdn.example.com/logo.png  ',
			}),
		);

		expect(parsed.logoUrl).toBe('https://cdn.example.com/logo.png');
	});
});

describe('toPlatformSettingsFormValues', () => {
	it('round-trips update request values into form values', () => {
		const request = parsePlatformSettingsForm(createValidFormValues());
		const formValues = toPlatformSettingsFormValues(request);

		expect(formValues.projectName).toBe(request.projectName);
		expect(formValues.logoSource).toBe(request.logoSource);
		expect(formValues.logoUrl).toBe('');
	});
});

describe('platformSettingsDtoToFormValues', () => {
	it('maps nested DTO fields into flat form values', () => {
		const dto = mapPlatformSettingsRecord(getPlatformSettingsSeedData());
		const formValues = platformSettingsDtoToFormValues(dto);

		expect(formValues.projectName).toBe(dto.projectName);
		expect(formValues.buttonSaveBg).toBe(dto.buttonColors.saveBg);
		expect(formValues.primaryBgLight).toBe(dto.themeColors.primary.light.bg);
		expect(formValues.menuItemActiveBgDark).toBe(
			dto.themeColors.menuNavigation.dark.itemActiveBg,
		);
	});
});

describe('mapThemeColorsFromRecord', () => {
	it('maps flat record fields into nested theme colors', () => {
		const request = parsePlatformSettingsForm(createValidFormValues());
		const themeColors = mapThemeColorsFromRecord(request);

		expect(themeColors.primary.light.bg).toBe(request.primaryBgLight);
		expect(themeColors.shellGradient.dark.to).toBe(request.shellGradientToDark);
		expect(themeColors.menuNavigation.light.itemHoverBg).toBe(
			request.menuItemHoverBgLight,
		);
	});
});
