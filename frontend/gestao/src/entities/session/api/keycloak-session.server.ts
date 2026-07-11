const keycloakProviderId = 'keycloak';
const endpoint = '/seguranca/sessoes';

/**
 * Invalida todas as sessões Keycloak do usuário (logout global no IdP).
 * O `userId` é o id better-auth (Prisma User); o identity Keycloak vem de Account.accountId.
 */
export async function invalidateKeycloakSessionsForUser(
	userId: string,
): Promise<void> {
	const identityId = await resolveKeycloakIdentityId(userId);

	if (!identityId) {
		throw new Error(
			'Conta Keycloak não encontrada para o usuário. Não foi possível invalidar a sessão no IdP.',
		);
	}

	const { fetchCadastros } =
		await import('@shared/api/cadastros/cadastros.server.ts');

	await fetchCadastros<void>(
		`${endpoint}/${encodeURIComponent(identityId)}`,
		{ method: 'DELETE' },
	);

	await clearKeycloakAccountTokens(userId);
}

async function resolveKeycloakIdentityId(
	userId: string,
): Promise<string | undefined> {
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		return undefined;
	}

	const account = await prismaClient.account.findFirst({
		select: { accountId: true },
		where: {
			providerId: keycloakProviderId,
			userId,
		},
	});

	return account?.accountId ?? undefined;
}

async function clearKeycloakAccountTokens(userId: string): Promise<void> {
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		return;
	}

	await prismaClient.account.updateMany({
		data: {
			accessToken: null,
			accessTokenExpiresAt: null,
			idToken: null,
			refreshToken: null,
			refreshTokenExpiresAt: null,
		},
		where: {
			providerId: keycloakProviderId,
			userId,
		},
	});
}
