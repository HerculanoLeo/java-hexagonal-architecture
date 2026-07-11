import type {MenuDto, MenuItemDto} from '@entities/menu';

export function isMenuActive(menu: MenuDto, pathname: string) {
	return menu.items.some((item) => isMenuItemActive(item, pathname));
}

export function isMenuItemActive(item: MenuItemDto, pathname: string): boolean {
	return (
		isRouteActive(item.route, pathname) ||
		Boolean(
			item.items?.some((childItem) => isMenuItemActive(childItem, pathname)),
		)
	);
}

export function isRouteActive(route: string | undefined, pathname: string) {
	if (!route) {
		return false;
	}

	return pathname === route || pathname.startsWith(`${route}/`);
}
