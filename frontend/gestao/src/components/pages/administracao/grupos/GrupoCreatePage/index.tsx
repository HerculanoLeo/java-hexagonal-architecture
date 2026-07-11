import {getRouteApi, useNavigate} from '@tanstack/react-router';
import {useMutation, useQueryClient} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import type {GrupoRegisterRequest} from '@entities/grupo';
import {grupoService, invalidateGruposList} from '@entities/grupo';
import {appToast} from '@shared/ui/toast';

import GrupoForm from '@components/features/administracao/grupos/GrupoForm';
import {gruposBreadcrumbs} from '@components/pages/administracao/grupos/lib/grupos.breadcrumbs';

const routeApi = getRouteApi(
	'/(authenticated)/administracao/acesso/grupos/novo/',
);

export function GrupoCreatePage() {
	const navigate = useNavigate();
	const queryClient = useQueryClient();
	const roles = routeApi.useLoaderData();

	const registerMutation = useMutation({
		mutationFn: (data: GrupoRegisterRequest) => grupoService.register(data),
		onSuccess: async () => {
			await invalidateGruposList(queryClient);
			appToast.sucesso({
				title: 'Grupo cadastrado',
				description: 'O grupo foi cadastrado com sucesso.',
			});
			await navigate({ to: '/administracao/acesso/grupos' });
		},
	});

	return (
		<ContentLayout breadcrumbs={gruposBreadcrumbs.create}>
			<GrupoForm
				isSubmitting={registerMutation.isPending}
				mode="create"
				onBack={() => void navigate({ to: '/administracao/acesso/grupos' })}
				onSubmit={(data) => registerMutation.mutateAsync(data)}
				roles={roles}
			/>
		</ContentLayout>
	);
}
