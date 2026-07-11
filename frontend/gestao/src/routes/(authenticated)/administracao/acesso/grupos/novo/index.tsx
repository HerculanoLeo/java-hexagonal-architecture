import {createFileRoute} from '@tanstack/react-router';

import {GrupoCreatePage} from '@components/pages/administracao/grupos/GrupoCreatePage';
import {ROLES_QUERY_KEY, rolesService} from '@entities/role';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/acesso/grupos/novo/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.GRUPOS],
		}),
	loader: ({ context: { queryClient } }) =>
		queryClient.ensureQueryData({
			queryKey: [ROLES_QUERY_KEY, 'findAll'],
			queryFn: () => rolesService.findAll(),
		}),
	component: GrupoCreatePage,
});
