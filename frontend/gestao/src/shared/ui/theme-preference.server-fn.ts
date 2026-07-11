import {createServerFn} from '@tanstack/react-start';
import {z} from 'zod';

const themePreferenceSchema = z.enum(['light', 'dark', 'system']);

export const persistThemePreference = createServerFn({ method: 'POST' })
	.validator(themePreferenceSchema)
	.handler(async ({ data: theme }) => {
		const { setThemePreferenceCookie } =
			await import('@shared/ui/theme-preference.server');

		setThemePreferenceCookie(theme);
	});
