import {createFileRoute} from '@tanstack/react-router';

import {UsuariosOnlineListPage} from '@components/pages/administracao/seguranca/UsuariosOnlineListPage';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/seguranca/usuarios-online/',
)({
	beforeLoad: async ({ context, location }) => {
		await routeRequiredRoles({
			location,
			roles: [SISTEMAS.SEGURANCA_USUARIOS_ONLINE],
		});

		return {
			currentSessionId: context.authContext.session.session.id,
			currentUserId: context.authContext.session.user.id,
		};
	},
	loader: ({ context }) => ({
		currentSessionId: context.currentSessionId,
		currentUserId: context.currentUserId,
	}),
	component: UsuariosOnlineListPage,
});
