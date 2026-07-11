import {createServerFn} from '@tanstack/react-start';

import {hasRole} from '@shared/auth/auth-permissions';

import type {MenuDto, MenuItemDto} from '../model/menu.dto';

export const getMenus = createServerFn({ method: 'GET' }).handler(async () => {
	const { requireAuthenticatedContextValue } =
		await import('@shared/auth/auth.session.server');
	const authContext = await requireAuthenticatedContextValue();

	return buildMenusForAuthContext(authContext);
});

async function buildMenusForAuthContext(
	authContext: Parameters<typeof hasRole>[0],
) {
	const { loadNavigationMenus } = await import('./menu.repository');
	const menus = await loadNavigationMenus();

	return filterMenusByRoles(menus, authContext);
}

function filterMenusByRoles(
	menusToFilter: MenuDto[],
	authContext: Parameters<typeof hasRole>[0],
) {
	return menusToFilter
		.map((menu) => {
			const items = filterMenuItemsByRoles(menu.items, authContext);

			if (
				!hasRole(authContext, { roles: menu.roles }) ||
				(menu.items.length > 0 && items.length === 0)
			) {
				return null;
			}

			return {
				...menu,
				items,
			};
		})
		.filter((menu): menu is MenuDto => Boolean(menu));
}

function filterMenuItemsByRoles(
	items: MenuItemDto[],
	authContext: Parameters<typeof hasRole>[0],
): MenuItemDto[] {
	const visibleItems: MenuItemDto[] = [];

	for (const item of items) {
		const childItems = item.items
			? filterMenuItemsByRoles(item.items, authContext)
			: undefined;
		const hasVisibleChildren = Boolean(childItems?.length);
		const hasNavigationTarget = Boolean(item.route || item.externalUrl);

		if (
			!hasRole(authContext, { roles: item.roles }) ||
			(!hasVisibleChildren && !hasNavigationTarget)
		) {
			continue;
		}

		visibleItems.push({
			...item,
			items: childItems,
		});
	}

	return visibleItems;
}
