import {useMutation} from '@tanstack/react-query';

import {ContentLayout} from '@components/widgets/content-layout';
import {authService} from '@entities/auth';
import {signOutWithKeycloak} from '@shared/auth/auth-client.configuration';
import {appToast} from '@shared/ui/toast';

import {TrocarSenhaForm} from '@components/features/me/TrocarSenhaForm';
import {trocarSenhaBreadcrumbs} from '@components/pages/me/lib/me.breadcrumbs';

export function TrocarSenhaPage() {
	const changePasswordMutation = useMutation({
		mutationFn: (data: Parameters<typeof authService.changePassword>[0]) =>
			authService.changePassword(data),
		onSuccess: async () => {
			appToast.sucesso({
				title: 'Senha alterada',
				description: 'Faça login novamente com a nova senha.',
			});
			await signOutWithKeycloak();
		},
	});

	return (
		<ContentLayout breadcrumbs={trocarSenhaBreadcrumbs}>
			<TrocarSenhaForm
				isSubmitting={changePasswordMutation.isPending}
				onSubmit={(data) => changePasswordMutation.mutateAsync(data)}
			/>
		</ContentLayout>
	);
}
