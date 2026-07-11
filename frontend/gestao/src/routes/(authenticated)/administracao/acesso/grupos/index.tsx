import {createFileRoute} from '@tanstack/react-router';

import {GruposListPage} from '@components/pages/administracao/grupos/GruposListPage';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/acesso/grupos/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.GRUPOS],
		}),
	component: GruposListPage,
});
