import {getRouteApi, useNavigate} from '@tanstack/react-router';
import {useMutation, useQueryClient} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import type {UsuarioRegisterRequest} from '@entities/usuario';
import {invalidateUsuariosList, usuariosService} from '@entities/usuario';
import {appToast} from '@shared/ui/toast';

import UsuarioForm from '@components/features/administracao/usuarios/UsuarioForm';
import {usuariosBreadcrumbs} from '@components/pages/administracao/usuarios/lib/usuarios.breadcrumbs';

const routeApi = getRouteApi(
	'/(authenticated)/administracao/acesso/usuarios/novo/',
);

export function UsuarioCreatePage() {
	const navigate = useNavigate();
	const queryClient = useQueryClient();
	const grupos = routeApi.useLoaderData();

	const registerMutation = useMutation({
		mutationFn: (data: UsuarioRegisterRequest) =>
			usuariosService.register(data),
		onSuccess: async () => {
			await invalidateUsuariosList(queryClient);
			appToast.sucesso({
				title: 'Usuário cadastrado',
				description: 'O usuário foi cadastrado com sucesso.',
			});
			await navigate({ to: '/administracao/acesso/usuarios' });
		},
	});

	return (
		<ContentLayout breadcrumbs={usuariosBreadcrumbs.create}>
			<UsuarioForm
				authenticatedUserId=""
				grupos={grupos}
				isSubmitting={registerMutation.isPending}
				mode="create"
				onBack={() => void navigate({ to: '/administracao/acesso/usuarios' })}
				onSubmit={(data: UsuarioRegisterRequest) =>
					registerMutation.mutateAsync(data)
				}
			/>
		</ContentLayout>
	);
}
