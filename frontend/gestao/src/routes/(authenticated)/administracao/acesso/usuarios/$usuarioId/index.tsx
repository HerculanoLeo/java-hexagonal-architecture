import {createFileRoute} from '@tanstack/react-router';

import {UsuarioEditPage} from '@components/pages/administracao/usuarios/UsuarioEditPage';
import {fetchUsuarioForEdit, getUsuarioEditQueryKey} from '@entities/usuario';
import {GRUPOS_QUERY_KEY, grupoService} from '@entities/grupo';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {routeRequiredRoles} from '@shared/auth/auth-guards.server-fn';

export const Route = createFileRoute(
	'/(authenticated)/administracao/acesso/usuarios/$usuarioId/',
)({
	beforeLoad: ({ location }) =>
		routeRequiredRoles({
			location,
			roles: [SISTEMAS.USUARIOS],
		}),
	loader: async ({ context: { queryClient }, params }) => {
		const [usuario, grupos] = await Promise.all([
			queryClient.ensureQueryData({
				queryKey: getUsuarioEditQueryKey(params.usuarioId),
				queryFn: () => fetchUsuarioForEdit(params.usuarioId),
			}),
			queryClient.ensureQueryData({
				queryKey: [GRUPOS_QUERY_KEY, 'findAll'],
				queryFn: () => grupoService.findGrupos(),
			}),
		]);

		return {
			grupos,
			usuario,
		};
	},
	component: UsuarioEditPage,
});
