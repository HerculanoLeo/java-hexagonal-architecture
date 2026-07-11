import {createFileRoute} from '@tanstack/react-router';

import {ConfiguracoesPage} from '@components/pages/administracao/configuracoes/ConfiguracoesPage';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';
import {getPlatformSettings} from '@entities/platform-settings';

export const Route = createFileRoute(
	'/(authenticated)/administracao/configuracoes/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.CONFIGURACAO],
		}),
	loader: () => getPlatformSettings(),
	component: ConfiguracoesPage,
});
