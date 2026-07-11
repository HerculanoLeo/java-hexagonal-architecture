import type {QueryClient} from '@tanstack/react-query';

import type MeusDadosProfileDto from '../model/meus-dados-profile.dto';
import authService, {AUTH_QUERY_KEY} from './auth.service';

export function getMeusDadosQueryKey() {
	return [AUTH_QUERY_KEY, 'meus-dados'] as const;
}

export async function fetchMeusDadosProfile(): Promise<MeusDadosProfileDto> {
	const [usuario, grupo] = await Promise.all([
		authService.me(),
		authService.findGrupo(),
	]);

	return {
		email: usuario.email ?? '',
		grupo: grupo ?? undefined,
		id: usuario.id,
		nome: usuario.nome,
		status: usuario.status,
	};
}

export async function invalidateMeusDadosProfile(queryClient: QueryClient) {
	await queryClient.invalidateQueries({
		queryKey: getMeusDadosQueryKey(),
	});
}

export async function refetchMeusDadosProfile(queryClient: QueryClient) {
	await queryClient.refetchQueries({
		queryKey: getMeusDadosQueryKey(),
	});
}
