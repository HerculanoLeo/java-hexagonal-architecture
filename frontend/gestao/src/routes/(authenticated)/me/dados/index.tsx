import {createFileRoute} from '@tanstack/react-router';

import {MeusDadosPage} from '@components/pages/me/MeusDadosPage';
import {fetchMeusDadosProfile, getMeusDadosQueryKey} from '@entities/auth';

export const Route = createFileRoute('/(authenticated)/me/dados/')({
	loader: ({ context: { queryClient } }) =>
		queryClient.ensureQueryData({
			queryKey: getMeusDadosQueryKey(),
			queryFn: fetchMeusDadosProfile,
		}),
	component: MeusDadosPage,
});
