import {getCookie, getRequestHeaders, setCookie,} from '@tanstack/react-start/server';

import {defaultThemePreference, themeCookieName,} from '@shared/ui/theme-preference.constants';
import type {ThemePreference} from '@shared/ui/theme-preference.guards';
import {isThemePreference} from '@shared/ui/theme-preference.guards';
import type {ResolvedTheme} from '@shared/ui/theme-preference.utils';

const cookiePath = '/';
const cookieMaxAge = 60 * 60 * 24 * 365;

export function getThemePreferenceCookie(): ThemePreference {
	const cookieValue = getCookie(themeCookieName);

	if (isThemePreference(cookieValue)) {
		return cookieValue;
	}

	return defaultThemePreference;
}

export function setThemePreferenceCookie(theme: ThemePreference) {
	setCookie(themeCookieName, theme, {
		httpOnly: false,
		maxAge: cookieMaxAge,
		path: cookiePath,
		sameSite: 'lax',
	});
}

export function resolveThemePreferenceOnServer(
	preference: ThemePreference,
): ResolvedTheme {
	if (preference === 'dark') {
		return 'dark';
	}

	if (preference === 'light') {
		return 'light';
	}

	const headers = getRequestHeaders();
	const prefersColorScheme =
		headers.get('sec-ch-prefers-color-scheme') ??
		headers.get('Sec-CH-Prefers-Color-Scheme');

	if (prefersColorScheme === 'dark') {
		return 'dark';
	}

	if (prefersColorScheme === 'light') {
		return 'light';
	}

	return 'light';
}

export function getResolvedThemeFromCookie(): ResolvedTheme {
	return resolveThemePreferenceOnServer(getThemePreferenceCookie());
}
