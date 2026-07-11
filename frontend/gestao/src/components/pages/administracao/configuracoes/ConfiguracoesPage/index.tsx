import {getRouteApi, useRouter} from '@tanstack/react-router';
import {useMutation, useQueryClient} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import {appToast} from '@shared/ui/toast';

import {ConfiguracoesForm} from '@components/features/administracao/configuracoes/ConfiguracoesForm';
import {configuracoesBreadcrumbs} from '@components/pages/administracao/configuracoes/lib/configuracoes.breadcrumbs';
import {invalidatePlatformSettings, savePlatformSettings,} from '@entities/platform-settings';

const routeApi = getRouteApi('/(authenticated)/administracao/configuracoes/');

export function ConfiguracoesPage() {
	const settings = routeApi.useLoaderData();
	const router = useRouter();
	const queryClient = useQueryClient();

	const updateMutation = useMutation({
		mutationFn: savePlatformSettings,
		onSuccess: async () => {
			await invalidatePlatformSettings(queryClient);
			await router.invalidate();
			appToast.sucesso({
				title: 'Configurações salvas',
				description:
					'As configurações do sistema foram atualizadas com sucesso.',
			});
		},
	});

	return (
		<ContentLayout breadcrumbs={configuracoesBreadcrumbs}>
			<ConfiguracoesForm
				key={settings.updatedAt}
				isSubmitting={updateMutation.isPending}
				onSubmit={(data) => updateMutation.mutateAsync(data)}
				settings={settings}
			/>
		</ContentLayout>
	);
}
