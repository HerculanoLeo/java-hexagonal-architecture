import {defaultThemePreference, themeChangeEvent, themeStorageKey,} from '@shared/ui/theme-preference.constants';
import type {ThemePreference} from '@shared/ui/theme-preference.guards';
import {isThemePreference} from '@shared/ui/theme-preference.guards';

export type ResolvedTheme = 'light' | 'dark';

export { defaultThemePreference, themeChangeEvent, themeStorageKey };
export type { ThemePreference };

export function getStoredThemePreference(): ThemePreference {
	if (typeof window === 'undefined') {
		return defaultThemePreference;
	}

	const storedTheme = localStorage.getItem(themeStorageKey);

	if (isThemePreference(storedTheme)) {
		return storedTheme;
	}

	return defaultThemePreference;
}

export function getThemePreferenceSnapshot(): ThemePreference {
	if (typeof window === 'undefined') {
		return defaultThemePreference;
	}

	const documentTheme = document.documentElement.dataset.theme;

	if (isThemePreference(documentTheme)) {
		return documentTheme;
	}

	return getStoredThemePreference();
}

export function resolveThemePreference(
	preference: ThemePreference,
): ResolvedTheme {
	if (typeof window === 'undefined') {
		return 'light';
	}

	const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

	return preference === 'dark' || (preference === 'system' && prefersDark)
		? 'dark'
		: 'light';
}

export function getResolvedThemeSnapshot(): ResolvedTheme {
	if (typeof document === 'undefined') {
		return 'light';
	}

	return document.documentElement.classList.contains('dark') ? 'dark' : 'light';
}

export function subscribeToThemePreference(onStoreChange: () => void) {
	if (typeof window === 'undefined') {
		return () => {};
	}

	const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
	const handleThemeChange = () => {
		onStoreChange();
	};

	const observer = new MutationObserver(handleThemeChange);

	observer.observe(document.documentElement, {
		attributeFilter: ['class', 'data-theme'],
		attributes: true,
	});

	window.addEventListener('storage', handleThemeChange);
	window.addEventListener(themeChangeEvent, handleThemeChange);
	mediaQuery.addEventListener('change', handleThemeChange);

	return () => {
		observer.disconnect();
		window.removeEventListener('storage', handleThemeChange);
		window.removeEventListener(themeChangeEvent, handleThemeChange);
		mediaQuery.removeEventListener('change', handleThemeChange);
	};
}

export function applyThemePreference(theme: ThemePreference) {
	if (typeof window === 'undefined') {
		return;
	}

	const shouldUseDarkTheme = resolveThemePreference(theme) === 'dark';

	document.documentElement.classList.toggle('dark', shouldUseDarkTheme);
	document.documentElement.dataset.theme = theme;
}

export function setThemePreference(theme: ThemePreference) {
	localStorage.setItem(themeStorageKey, theme);
	applyThemePreference(theme);
	window.dispatchEvent(new Event(themeChangeEvent));
	void persistThemePreference({ data: theme });
}

async function persistThemePreference({ data }: { data: ThemePreference }) {
	const { persistThemePreference: persistThemePreferenceOnServer } =
		await import('@shared/ui/theme-preference.server-fn');

	await persistThemePreferenceOnServer({ data });
}
