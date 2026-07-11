import type {MenuDto} from '../model/menu.dto';
import {buildMenuTreeFromNodes} from '../lib/menu-tree.utils';
import {navigationMenusCache} from '../lib/menu.cache';
import {getDefaultNavigationMenus} from '../lib/menu.defaults';

export async function loadNavigationMenus(): Promise<MenuDto[]> {
	const cached = navigationMenusCache.get();

	if (cached) {
		return cached;
	}

	const menus = await fetchNavigationMenusFromDatabase();
	navigationMenusCache.set(menus);

	return menus;
}

async function fetchNavigationMenusFromDatabase(): Promise<MenuDto[]> {
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		return getDefaultNavigationMenus();
	}

	const nodes = await prismaClient.menu.findMany({
		orderBy: [{ sortOrder: 'asc' }],
		where: { enabled: true },
	});

	if (nodes.length === 0) {
		return getDefaultNavigationMenus();
	}

	return buildMenuTreeFromNodes(nodes);
}
