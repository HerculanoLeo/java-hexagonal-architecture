import type {UsuarioOnlineGroupDto, UsuarioOnlineSessionDto,} from '../model/usuario-online.dto';

export async function loadUsuariosOnline(): Promise<UsuarioOnlineGroupDto[]> {
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		return [];
	}

	const now = new Date();
	const sessions = await prismaClient.session.findMany({
		include: { user: true },
		orderBy: { updatedAt: 'desc' },
		where: { expiresAt: { gt: now } },
	});

	const groups = new Map<string, UsuarioOnlineGroupDto>();

	for (const session of sessions) {
		const sessionDto: UsuarioOnlineSessionDto = {
			createdAt: session.createdAt.toISOString(),
			expiresAt: session.expiresAt.toISOString(),
			id: session.id,
			ipAddress: session.ipAddress,
			updatedAt: session.updatedAt.toISOString(),
			userAgent: session.userAgent,
		};

		const existingGroup = groups.get(session.userId);

		if (!existingGroup) {
			groups.set(session.userId, {
				lastActivityAt: session.updatedAt.toISOString(),
				sessionCount: 1,
				sessions: [sessionDto],
				userEmail: session.user.email,
				userId: session.userId,
				userName: session.user.name,
			});
			continue;
		}

		existingGroup.sessions.push(sessionDto);
		existingGroup.sessionCount = existingGroup.sessions.length;

		if (session.updatedAt.toISOString() > existingGroup.lastActivityAt) {
			existingGroup.lastActivityAt = session.updatedAt.toISOString();
		}
	}

	return Array.from(groups.values()).sort((left, right) =>
		right.lastActivityAt.localeCompare(left.lastActivityAt),
	);
}

export async function revokeUsuarioSession(sessionId: string): Promise<void> {
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		throw new Error(
			'Banco de dados não configurado. Defina DATABASE_URL para revogar sessões.',
		);
	}

	const session = await prismaClient.session.findUnique({
		select: { userId: true },
		where: { id: sessionId },
	});

	if (!session) {
		const notFound = new Error('Sessão não encontrada.');
		Object.assign(notFound, { code: 'P2025' });
		throw notFound;
	}

	const { invalidateKeycloakSessionsForUser } =
		await import('./keycloak-session.server');

	await invalidateKeycloakSessionsForUser(session.userId);

	await prismaClient.session.delete({
		where: { id: sessionId },
	});
}

export async function revokeAllUsuarioSessions(
	userId: string,
): Promise<{ revokedCount: number }> {
	const { prismaClient } = await import('@shared/database/prisma-client');

	if (!prismaClient) {
		throw new Error(
			'Banco de dados não configurado. Defina DATABASE_URL para revogar sessões.',
		);
	}

	const { invalidateKeycloakSessionsForUser } =
		await import('./keycloak-session.server');

	await invalidateKeycloakSessionsForUser(userId);

	const result = await prismaClient.session.deleteMany({
		where: { userId },
	});

	return { revokedCount: result.count };
}
