import {SISTEMAS} from '@shared/auth/auth-permissions.enum';

import {DEFAULT_MENU_NODES, getDefaultNavigationMenus,} from './menu.defaults';

describe('getDefaultNavigationMenus', () => {
	it('returns exactly one root menu', () => {
		const menus = getDefaultNavigationMenus();

		expect(menus).toHaveLength(1);
		expect(menus[0].label).toBe('Administração');
	});

	it('builds the expected access and security structure', () => {
		const menus = getDefaultNavigationMenus();
		const administracao = menus[0];

		const acesso = administracao.items?.find((item) => item.label === 'Acesso');
		const seguranca = administracao.items?.find((item) => item.label === 'Segurança');

		expect(acesso).toBeDefined();
		expect(seguranca).toBeDefined();

		const acessoLabels = acesso?.items?.map((item) => item.label);
		expect(acessoLabels).toEqual(['Usuários', 'Grupos']);

		const segurancaLabels = seguranca?.items?.map((item) => item.label);
		expect(segurancaLabels).toEqual([
			'Usuários Online',
			'Histórico de logins',
		]);
	});

	it('defines route and roles for leaf menu items', () => {
		const menus = getDefaultNavigationMenus();
		const administracao = menus[0];

		const usuarios = administracao.items
			?.find((item) => item.label === 'Acesso')
			?.items?.find((item) => item.label === 'Usuários');

		expect(usuarios?.route).toBe('/administracao/acesso/usuarios');
		expect(usuarios?.roles).toEqual([SISTEMAS.USUARIOS]);
	});

	it('keeps DEFAULT_MENU_NODES aligned with the flat node count', () => {
		expect(DEFAULT_MENU_NODES).toHaveLength(8);
	});
});
