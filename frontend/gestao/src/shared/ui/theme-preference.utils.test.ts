import {defaultThemePreference, themeChangeEvent, themeStorageKey,} from '@shared/ui/theme-preference.constants';

import {
    applyThemePreference,
    getResolvedThemeSnapshot,
    getStoredThemePreference,
    getThemePreferenceSnapshot,
    resolveThemePreference,
    subscribeToThemePreference,
} from './theme-preference.utils';

function mockMatchMedia(matchesDark = false) {
	Object.defineProperty(window, 'matchMedia', {
		writable: true,
		value: vi.fn().mockImplementation((query: string) => ({
			matches: matchesDark && query === '(prefers-color-scheme: dark)',
			media: query,
			onchange: null,
			addEventListener: vi.fn(),
			removeEventListener: vi.fn(),
			addListener: vi.fn(),
			removeListener: vi.fn(),
			dispatchEvent: vi.fn(),
		})),
	});
}

describe('getStoredThemePreference', () => {
	beforeEach(() => {
		localStorage.clear();
	});

	it('returns the default when storage is empty', () => {
		expect(getStoredThemePreference()).toBe(defaultThemePreference);
	});

	it('returns a valid stored preference', () => {
		localStorage.setItem(themeStorageKey, 'dark');

		expect(getStoredThemePreference()).toBe('dark');
	});
});

describe('getThemePreferenceSnapshot', () => {
	beforeEach(() => {
		localStorage.clear();
		delete document.documentElement.dataset.theme;
	});

	it('prefers the document dataset theme', () => {
		document.documentElement.dataset.theme = 'light';

		expect(getThemePreferenceSnapshot()).toBe('light');
	});
});

describe('resolveThemePreference', () => {
	beforeEach(() => {
		mockMatchMedia(false);
	});

	it('resolves explicit light and dark preferences', () => {
		expect(resolveThemePreference('light')).toBe('light');
		expect(resolveThemePreference('dark')).toBe('dark');
	});

	it('resolves system preference from matchMedia', () => {
		mockMatchMedia(true);

		expect(resolveThemePreference('system')).toBe('dark');
	});
});

describe('getResolvedThemeSnapshot', () => {
	it('reads the current document theme class', () => {
		document.documentElement.classList.add('dark');

		expect(getResolvedThemeSnapshot()).toBe('dark');

		document.documentElement.classList.remove('dark');
		expect(getResolvedThemeSnapshot()).toBe('light');
	});
});

describe('applyThemePreference', () => {
	beforeEach(() => {
		mockMatchMedia(false);
		document.documentElement.classList.remove('dark');
		delete document.documentElement.dataset.theme;
	});

	it('applies dark theme classes and dataset', () => {
		applyThemePreference('dark');

		expect(document.documentElement.classList.contains('dark')).toBe(true);
		expect(document.documentElement.dataset.theme).toBe('dark');
	});
});

describe('subscribeToThemePreference', () => {
	beforeEach(() => {
		mockMatchMedia(false);
	});

	it('returns an unsubscribe function', () => {
		const unsubscribe = subscribeToThemePreference(vi.fn());

		expect(typeof unsubscribe).toBe('function');
		unsubscribe();
	});
});

describe('theme constants', () => {
	it('exports stable theme event and storage keys', () => {
		expect(themeStorageKey).toBe('starter:theme');
		expect(themeChangeEvent).toBe('starter:theme-change');
	});
});
