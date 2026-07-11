import {getRouteApi, useNavigate} from '@tanstack/react-router';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import type {GrupoUpdateRequest} from '@entities/grupo';
import {
    fetchGrupoForEdit,
    getGrupoEditQueryKey,
    getGrupoErrorMessage,
    grupoService,
    invalidateGruposList,
} from '@entities/grupo';
import {appToast} from '@shared/ui/toast';

import GrupoForm from '@components/features/administracao/grupos/GrupoForm';
import {gruposBreadcrumbs} from '@components/pages/administracao/grupos/lib/grupos.breadcrumbs';

const routeApi = getRouteApi(
	'/(authenticated)/administracao/acesso/grupos/$grupoId/',
);

export function GrupoEditPage() {
	const { grupoId } = routeApi.useParams();
	const { roles } = routeApi.useLoaderData();
	const navigate = useNavigate();
	const queryClient = useQueryClient();

	const grupoQuery = useQuery({
		queryKey: getGrupoEditQueryKey(grupoId),
		queryFn: () => fetchGrupoForEdit(grupoId),
	});

	const grupo = grupoQuery.data;

	const updateMutation = useMutation({
		mutationFn: (data: GrupoUpdateRequest) =>
			grupoService.update(grupoId, data),
		onSuccess: async () => {
			await invalidateGruposList(queryClient);
			appToast.sucesso({
				title: 'Grupo atualizado',
				description: 'Os dados do grupo foram atualizados com sucesso.',
			});
			await navigate({ to: '/administracao/acesso/grupos' });
		},
	});

	const deleteMutation = useMutation({
		mutationFn: () => grupoService.delete(grupoId),
		onSuccess: async () => {
			await invalidateGruposList(queryClient);
			appToast.sucesso({
				title: 'Grupo excluído',
				description: 'O grupo foi excluído com sucesso.',
			});
			await navigate({ to: '/administracao/acesso/grupos' });
		},
		onError: (error) => {
			appToast.falha({
				title: 'Erro ao excluir grupo',
				description: getGrupoErrorMessage(error),
			});
		},
	});

	if (!grupo) {
		return null;
	}

	return (
		<ContentLayout breadcrumbs={gruposBreadcrumbs.edit(grupo.nome)}>
			<GrupoForm
				deleteActionLoading={deleteMutation.isPending}
				grupo={grupo}
				isSubmitting={updateMutation.isPending}
				mode="edit"
				onBack={() => void navigate({ to: '/administracao/acesso/grupos' })}
				onDelete={() => deleteMutation.mutate()}
				onSubmit={(data) => updateMutation.mutateAsync(data)}
				roles={roles}
			/>
		</ContentLayout>
	);
}
