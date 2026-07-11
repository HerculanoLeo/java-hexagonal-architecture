import {createFileRoute} from '@tanstack/react-router';

import {HistoricoLoginsListPage} from '@components/pages/administracao/seguranca/HistoricoLoginsListPage';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/seguranca/historico-logins/',
)({
	beforeLoad: async ({ location }) => {
		await routeRequiredRoles({
			location,
			roles: [SISTEMAS.SEGURANCA_HISTORICO_LOGINS],
		});
	},
	component: HistoricoLoginsListPage,
});
