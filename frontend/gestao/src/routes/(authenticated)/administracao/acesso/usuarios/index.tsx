import {createFileRoute} from '@tanstack/react-router';

import {UsuariosListPage} from '@components/pages/administracao/usuarios/UsuariosListPage';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/acesso/usuarios/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.USUARIOS],
		}),
	component: UsuariosListPage,
});
