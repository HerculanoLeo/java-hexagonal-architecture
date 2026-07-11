import {createServerFn} from '@tanstack/react-start';

import type RoleDto from '../model/role.dto';

export const ROLES_QUERY_KEY = 'roles';

const endpoint = '/sistema/roles';

const findRoles = createServerFn({ method: 'GET' }).handler(
	async (): Promise<RoleDto[]> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		return fetchCadastros<RoleDto[]>(endpoint);
	},
);

class RolesService {
	async findAll(): Promise<RoleDto[]> {
		return findRoles();
	}
}

export default new RolesService();
