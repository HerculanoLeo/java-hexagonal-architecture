import type {QueryClient} from '@tanstack/react-query';

import {
    getUsuariosOnline,
    revokeAllUsuarioSessions,
    revokeUsuarioSession,
    USUARIOS_ONLINE_QUERY_KEY,
} from './usuarios-online.server-fn';

export function getUsuariosOnlineQueryKey() {
	return [USUARIOS_ONLINE_QUERY_KEY, 'list'] as const;
}

export function fetchUsuariosOnline() {
	return getUsuariosOnline();
}

export function revokeSession(sessionId: string) {
	return revokeUsuarioSession({
		data: { sessionId },
	});
}

export function revokeAllSessions(userId: string) {
	return revokeAllUsuarioSessions({
		data: { userId },
	});
}

export function invalidateUsuariosOnline(queryClient: QueryClient) {
	return queryClient.invalidateQueries({
		queryKey: getUsuariosOnlineQueryKey(),
	});
}

export function getUsuariosOnlineErrorMessage(error: unknown) {
	if (error instanceof Error && error.message) {
		return error.message;
	}

	return 'Não foi possível concluir a operação. Tente novamente.';
}
