import {createFileRoute} from '@tanstack/react-router';

import {GrupoEditPage} from '@components/pages/administracao/grupos/GrupoEditPage';
import {fetchGrupoForEdit, getGrupoEditQueryKey} from '@entities/grupo';
import {ROLES_QUERY_KEY, rolesService} from '@entities/role';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/acesso/grupos/$grupoId/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.GRUPOS],
		}),
	loader: async ({ context: { queryClient }, params }) => {
		const [grupo, roles] = await Promise.all([
			queryClient.ensureQueryData({
				queryKey: getGrupoEditQueryKey(params.grupoId),
				queryFn: () => fetchGrupoForEdit(params.grupoId),
			}),
			queryClient.ensureQueryData({
				queryKey: [ROLES_QUERY_KEY, 'findAll'],
				queryFn: () => rolesService.findAll(),
			}),
		]);

		return {
			grupo,
			roles,
		};
	},
	component: GrupoEditPage,
});
