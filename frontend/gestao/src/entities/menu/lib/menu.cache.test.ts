import type {MenuDto} from '@entities/menu';

import {clearNavigationMenusCache, navigationMenusCache, setNavigationMenusCache,} from './menu.cache';

describe('navigationMenusCache', () => {
	const menus: MenuDto[] = [{ id: 'admin', label: 'Administração', items: [] }];

	afterEach(() => {
		clearNavigationMenusCache();
	});

	it('stores menus through setNavigationMenusCache', () => {
		setNavigationMenusCache(menus);

		expect(navigationMenusCache.get()).toEqual(menus);
	});

	it('clears cached menus', () => {
		setNavigationMenusCache(menus);
		clearNavigationMenusCache();

		expect(navigationMenusCache.get()).toBeUndefined();
	});
});
