import type {MenuDto} from '../model/menu.dto';
import type {CacheStore} from '@shared/cache/cache-store.type';
import {createInMemoryCacheStore} from '@shared/cache/cache-store.type';

export type NavigationMenusCacheStore = CacheStore<MenuDto[]>;

export const navigationMenusCache: NavigationMenusCacheStore =
	createInMemoryCacheStore<MenuDto[]>('navigationMenusCache');

export function clearNavigationMenusCache() {
	navigationMenusCache.clear();
}

export function setNavigationMenusCache(menus: MenuDto[]) {
	navigationMenusCache.set(menus);
}
