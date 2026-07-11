import type {MenuLayout} from '@entities/platform-settings';

export function getMenuLayoutFlags(menuLayout: MenuLayout) {
	return {
		hasFixedSidebar: menuLayout === 'sidebar',
		showMenuBar: menuLayout === 'hybrid',
	};
}
