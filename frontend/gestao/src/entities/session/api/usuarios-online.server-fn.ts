import {createServerFn} from '@tanstack/react-start';

import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {hasRole} from '@shared/auth/auth-permissions';
import type {UsuarioOnlineGroupDto} from '../model/usuario-online.dto';
import {revokeAllSessionsSchema} from '../model/revoke-all-sessions.schema';
import {revokeSessionSchema} from '../model/revoke-session.schema';

export const USUARIOS_ONLINE_QUERY_KEY = 'usuarios-online';

export const getUsuariosOnline = createServerFn({ method: 'GET' }).handler(
	async (): Promise<UsuarioOnlineGroupDto[]> => {
		await requireSegurancaUsuariosOnlineRole();

		const { loadUsuariosOnline } = await import('./usuarios-online.repository');

		return loadUsuariosOnline();
	},
);

export const revokeUsuarioSession = createServerFn({ method: 'POST' })
	.validator(revokeSessionSchema)
	.handler(async ({ data }): Promise<void> => {
		await requireSegurancaUsuariosOnlineRole();

		const { revokeUsuarioSession: revokeSession } =
			await import('./usuarios-online.repository');

		try {
			await revokeSession(data.sessionId);
		} catch (error) {
			if (isPrismaNotFoundError(error)) {
				throw new Error('Sessão não encontrada.');
			}

			await rethrowRevokeError(error);
		}
	});

export const revokeAllUsuarioSessions = createServerFn({ method: 'POST' })
	.validator(revokeAllSessionsSchema)
	.handler(async ({ data }): Promise<{ revokedCount: number }> => {
		await requireSegurancaUsuariosOnlineRole();

		const { revokeAllUsuarioSessions: revokeAllSessions } =
			await import('./usuarios-online.repository');

		try {
			return await revokeAllSessions(data.userId);
		} catch (error) {
			await rethrowRevokeError(error);
		}
	});

async function requireSegurancaUsuariosOnlineRole() {
	const { requireAuthenticatedContextValue } =
		await import('@shared/auth/auth.session.server');
	const authContext = await requireAuthenticatedContextValue();

	if (!hasRole(authContext, { roles: [SISTEMAS.SEGURANCA_USUARIOS_ONLINE] })) {
		throw new Error('Unauthorized');
	}

	return authContext;
}

function isPrismaNotFoundError(error: unknown) {
	return (
		typeof error === 'object' &&
		error !== null &&
		'code' in error &&
		error.code === 'P2025'
	);
}

async function rethrowRevokeError(error: unknown): Promise<never> {
	const { ApiHttpError } = await import('@shared/api/api-error.type.ts');

	if (error instanceof ApiHttpError) {
		throw new Error(
			error.message ||
				'Não foi possível invalidar a sessão no Keycloak. Tente novamente.',
		);
	}

	if (error instanceof Error) {
		throw error;
	}

	throw new Error('Não foi possível revogar a sessão. Tente novamente.');
}
