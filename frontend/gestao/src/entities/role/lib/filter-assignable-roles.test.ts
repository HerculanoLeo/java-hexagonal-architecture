import {SISTEMAS} from '@shared/auth/auth-permissions.enum';

import {filterAssignableSistemaRoles} from './filter-assignable-roles';

describe('filterAssignableSistemaRoles', () => {
	it('excludes admin-sistemas from group-assignable roles', () => {
		const roles = [
			{
				descricao: 'Acesso Total',
				id: '1',
				nome: SISTEMAS.ADMIN,
			},
			{
				descricao: 'Gestão de Usuários',
				id: '2',
				nome: SISTEMAS.USUARIOS,
			},
		];

		expect(filterAssignableSistemaRoles(roles)).toEqual([
			{
				descricao: 'Gestão de Usuários',
				id: '2',
				nome: SISTEMAS.USUARIOS,
			},
		]);
	});
});
