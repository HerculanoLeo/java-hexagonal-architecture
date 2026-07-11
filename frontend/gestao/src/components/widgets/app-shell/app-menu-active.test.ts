import type {MenuDto, MenuItemDto} from '@entities/menu';

import {isMenuActive, isMenuItemActive, isRouteActive,} from './app-menu-active';

describe('isRouteActive', () => {
	it('returns false when route is undefined', () => {
		expect(isRouteActive(undefined, '/administracao')).toBe(false);
	});

	it('matches exact paths and nested routes', () => {
		expect(isRouteActive('/administracao/acesso', '/administracao/acesso')).toBe(
			true,
		);
		expect(
			isRouteActive('/administracao/acesso', '/administracao/acesso/usuarios'),
		).toBe(true);
		expect(isRouteActive('/administracao/acesso', '/administracao/seguranca')).toBe(
			false,
		);
	});
});

describe('isMenuItemActive', () => {
	const item: MenuItemDto = {
		id: 'acesso',
		label: 'Acesso',
		items: [
			{
				id: 'usuarios',
				label: 'Usuários',
				route: '/administracao/acesso/usuarios',
			},
		],
	};

	it('returns true when a nested child route is active', () => {
		expect(isMenuItemActive(item, '/administracao/acesso/usuarios')).toBe(true);
	});

	it('returns false when no child route matches', () => {
		expect(isMenuItemActive(item, '/administracao/configuracoes')).toBe(false);
	});
});

describe('isMenuActive', () => {
	const menu: MenuDto = {
		id: 'administracao',
		label: 'Administração',
		items: [
			{
				id: 'configuracoes',
				label: 'Configurações',
				route: '/administracao/configuracoes',
			},
		],
	};

	it('returns true when any top-level item matches the pathname', () => {
		expect(isMenuActive(menu, '/administracao/configuracoes')).toBe(true);
	});

	it('returns false when no item matches the pathname', () => {
		expect(isMenuActive(menu, '/administracao/acesso/usuarios')).toBe(false);
	});
});
