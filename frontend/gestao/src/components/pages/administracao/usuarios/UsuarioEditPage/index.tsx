import {getRouteApi, useNavigate} from '@tanstack/react-router';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import type {UsuarioUpdateRequest} from '@entities/usuario';
import {
    fetchUsuarioForEdit,
    getUsuarioEditQueryKey,
    getUsuarioErrorMessage,
    invalidateUsuariosList,
    refetchUsuarioForEdit,
    usuariosService,
} from '@entities/usuario';
import {appToast} from '@shared/ui/toast';

import UsuarioForm from '@components/features/administracao/usuarios/UsuarioForm';
import {usuariosBreadcrumbs} from '@components/pages/administracao/usuarios/lib/usuarios.breadcrumbs';

const authenticatedRoute = getRouteApi('/(authenticated)');
const routeApi = getRouteApi(
	'/(authenticated)/administracao/acesso/usuarios/$usuarioId/',
);

export function UsuarioEditPage() {
	const { usuarioId } = routeApi.useParams();
	const { usuario: authenticatedUser } = authenticatedRoute.useLoaderData();
	const { grupos } = routeApi.useLoaderData();
	const navigate = useNavigate();
	const queryClient = useQueryClient();

	const usuarioQuery = useQuery({
		queryKey: getUsuarioEditQueryKey(usuarioId),
		queryFn: () => fetchUsuarioForEdit(usuarioId),
	});

	const usuario = usuarioQuery.data;

	const updateMutation = useMutation({
		mutationFn: (data: UsuarioUpdateRequest) =>
			usuariosService.update(usuarioId, data),
		onSuccess: async () => {
			await invalidateUsuariosList(queryClient);
			appToast.sucesso({
				title: 'Usuário atualizado',
				description: 'Os dados do usuário foram atualizados com sucesso.',
			});
			await navigate({ to: '/administracao/acesso/usuarios' });
		},
	});

	const ativarMutation = useMutation({
		mutationFn: () => usuariosService.ativar(usuarioId),
		onSuccess: async () => {
			await refetchUsuarioForEdit(queryClient, usuarioId);
			appToast.sucesso({
				title: 'Usuário ativado',
				description: 'O usuário foi ativado com sucesso.',
			});
		},
		onError: (error) => {
			appToast.falha({
				title: 'Erro ao ativar usuário',
				description: getUsuarioErrorMessage(error),
			});
		},
	});

	const inativarMutation = useMutation({
		mutationFn: () => usuariosService.inativar(usuarioId),
		onSuccess: async () => {
			await refetchUsuarioForEdit(queryClient, usuarioId);
			appToast.sucesso({
				title: 'Usuário inativado',
				description: 'O usuário foi inativado com sucesso.',
			});
		},
		onError: (error) => {
			appToast.falha({
				title: 'Erro ao inativar usuário',
				description: getUsuarioErrorMessage(error),
			});
		},
	});

	const resetPasswordMutation = useMutation({
		mutationFn: () => usuariosService.resetPassword(usuarioId),
		onSuccess: () => {
			appToast.sucesso({
				title: 'Redefinição de senha enviada',
				description:
					'Um e-mail foi enviado para o usuário redefinir a senha.',
			});
		},
		onError: (error) => {
			appToast.falha({
				title: 'Erro ao redefinir senha',
				description: getUsuarioErrorMessage(error),
			});
		},
	});

	if (!usuario) {
		return null;
	}

	return (
		<ContentLayout breadcrumbs={usuariosBreadcrumbs.edit(usuario.nome)}>
			<UsuarioForm
				authenticatedUserId={authenticatedUser.id}
				grupos={grupos}
				isSubmitting={updateMutation.isPending}
				mode="edit"
				onAtivar={() => ativarMutation.mutate()}
				onBack={() => void navigate({ to: '/administracao/acesso/usuarios' })}
				onInativar={() => inativarMutation.mutate()}
				onResetPassword={() => resetPasswordMutation.mutate()}
				onSubmit={(data) => updateMutation.mutateAsync(data)}
				statusActionLoading={
					ativarMutation.isPending ||
					inativarMutation.isPending ||
					resetPasswordMutation.isPending
				}
				usuario={usuario}
			/>
		</ContentLayout>
	);
}
