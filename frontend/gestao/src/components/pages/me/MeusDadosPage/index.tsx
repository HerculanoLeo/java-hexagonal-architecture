import {useRouter} from '@tanstack/react-router';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import {authService, fetchMeusDadosProfile, getMeusDadosQueryKey, refetchMeusDadosProfile,} from '@entities/auth';
import {appToast} from '@shared/ui/toast';

import {MeusDadosForm} from '@components/features/me/MeusDadosForm';
import {meusDadosBreadcrumbs} from '@components/pages/me/lib/me.breadcrumbs';

export function MeusDadosPage() {
	const router = useRouter();
	const queryClient = useQueryClient();

	const profileQuery = useQuery({
		queryKey: getMeusDadosQueryKey(),
		queryFn: fetchMeusDadosProfile,
	});

	const profile = profileQuery.data;

	const updateMutation = useMutation({
		mutationFn: (data: Parameters<typeof authService.updateMe>[0]) =>
			authService.updateMe(data),
		onSuccess: async () => {
			await refetchMeusDadosProfile(queryClient);
			await router.invalidate();
			appToast.sucesso({
				title: 'Dados atualizados',
				description: 'Suas informações foram atualizadas com sucesso.',
			});
		},
	});

	if (!profile) {
		return null;
	}

	return (
		<ContentLayout breadcrumbs={meusDadosBreadcrumbs}>
			<MeusDadosForm
				key={`${profile.id}-${profile.nome}-${profile.grupo?.id ?? 'sem-grupo'}`}
				isSubmitting={updateMutation.isPending}
				onSubmit={(data) => updateMutation.mutateAsync(data)}
				profile={profile}
			/>
		</ContentLayout>
	);
}
