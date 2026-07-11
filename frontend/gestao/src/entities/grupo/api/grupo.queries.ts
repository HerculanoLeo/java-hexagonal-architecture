import type {QueryClient} from '@tanstack/react-query';

import grupoService, {GRUPOS_QUERY_KEY} from './grupo.service';

export function getGrupoEditQueryKey(grupoId: string) {
	return [GRUPOS_QUERY_KEY, 'edit', grupoId] as const;
}

export async function fetchGrupoForEdit(grupoId: string) {
	return grupoService.findById(grupoId);
}

export async function invalidateGruposList(queryClient: QueryClient) {
	await queryClient.invalidateQueries({
		queryKey: [GRUPOS_QUERY_KEY, 'findAll'],
	});
}

export async function refetchGrupoForEdit(
	queryClient: QueryClient,
	grupoId: string,
) {
	await Promise.all([
		invalidateGruposList(queryClient),
		queryClient.refetchQueries({
			queryKey: getGrupoEditQueryKey(grupoId),
		}),
	]);
}

export function getGrupoErrorMessage(error: unknown) {
	return error instanceof Error
		? error.message
		: 'Não foi possível concluir a operação. Tente novamente.';
}
