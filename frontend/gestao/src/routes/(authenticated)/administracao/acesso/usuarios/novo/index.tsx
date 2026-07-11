import {createFileRoute} from '@tanstack/react-router';

import {UsuarioCreatePage} from '@components/pages/administracao/usuarios/UsuarioCreatePage';
import {GRUPOS_QUERY_KEY, grupoService} from '@entities/grupo';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/acesso/usuarios/novo/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.USUARIOS],
		}),
	loader: ({ context: { queryClient } }) =>
		queryClient.ensureQueryData({
			queryKey: [GRUPOS_QUERY_KEY, 'findAll'],
			queryFn: () => grupoService.findGrupos(),
		}),
	component: UsuarioCreatePage,
});
