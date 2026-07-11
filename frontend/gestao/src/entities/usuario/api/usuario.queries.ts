import type {QueryClient} from '@tanstack/react-query';

import type UsuarioDto from '../model/usuario.dto';

import usuariosService, {USUARIOS_QUERY_KEY} from './usuario.service';

export function getUsuarioEditQueryKey(usuarioId: string) {
	return [USUARIOS_QUERY_KEY, 'edit', usuarioId] as const;
}

export async function fetchUsuarioForEdit(
	usuarioId: string,
): Promise<UsuarioDto> {
	const [usuario, grupo] = await Promise.all([
		usuariosService.findById(usuarioId),
		usuariosService.findGrupoById(usuarioId),
	]);

	return {
		...usuario,
		...(grupo ? { grupo } : {}),
	};
}

export async function invalidateUsuariosList(queryClient: QueryClient) {
	await queryClient.invalidateQueries({
		queryKey: [USUARIOS_QUERY_KEY, 'findAll'],
	});
}

export async function refetchUsuarioForEdit(
	queryClient: QueryClient,
	usuarioId: string,
) {
	await Promise.all([
		invalidateUsuariosList(queryClient),
		queryClient.refetchQueries({
			queryKey: getUsuarioEditQueryKey(usuarioId),
		}),
	]);
}

export function getUsuarioErrorMessage(error: unknown) {
	return error instanceof Error
		? error.message
		: 'Não foi possível concluir a operação. Tente novamente.';
}
