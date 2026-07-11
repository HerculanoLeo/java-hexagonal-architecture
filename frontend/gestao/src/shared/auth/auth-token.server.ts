import type {AuthSession} from '@shared/auth/auth.configuration';
import {auth} from '@shared/auth/auth.configuration';
import {prismaClient} from '@shared/database/prisma-client';

const keycloakProviderId = 'keycloak';

export type AuthProviderToken = Awaited<
	ReturnType<typeof auth.api.getAccessToken>
>;

export type AuthenticatedAccessToken = {
	accessToken: string;
	session: AuthSession;
	token: AuthProviderToken;
};

type CachedTokenLookup = {
	expiresAt: number;
	promise: Promise<AuthProviderToken | undefined>;
};

const tokenLookupCache = new Map<string, CachedTokenLookup>();
const tokenLookupCacheTtlMs = 1_000;

export async function getSession(headers: Headers) {
	return auth.api.getSession({ headers });
}

export async function getSessionAccessToken(
	headers: Headers,
): Promise<AuthenticatedAccessToken | null> {
	const session = await getSession(headers);

	if (!session) {
		return null;
	}

	const token = await getKeycloakAccessToken(session.user.id);
	const accessToken = token?.accessToken;

	if (!accessToken) {
		return null;
	}

	return {
		accessToken,
		session,
		token,
	};
}

export async function getKeycloakIdTokenHint(userId: string) {
	const accountIdToken = await getKeycloakAccountIdToken(userId);

	if (accountIdToken) {
		return accountIdToken;
	}

	const token = await getKeycloakAccessToken(userId);

	return token?.idToken;
}

async function getKeycloakAccountIdToken(userId: string) {
	if (!prismaClient) {
		return undefined;
	}

	const account = await prismaClient.account.findFirst({
		select: {
			idToken: true,
		},
		where: {
			providerId: keycloakProviderId,
			userId,
		},
	});

	return account?.idToken ?? undefined;
}

export async function getKeycloakAccessToken(userId: string) {
	const cacheKey = `keycloak:${userId}`;
	const cachedTokenLookup = tokenLookupCache.get(cacheKey);
	const now = Date.now();

	if (cachedTokenLookup && cachedTokenLookup.expiresAt > now) {
		return cachedTokenLookup.promise;
	}

	const promise = auth.api
		.getAccessToken({
			body: {
				providerId: keycloakProviderId,
				userId,
			},
		})
		.catch(() => undefined);

	const tokenLookup: CachedTokenLookup = {
		expiresAt: now + tokenLookupCacheTtlMs,
		promise,
	};

	tokenLookupCache.set(cacheKey, tokenLookup);
	setTimeout(() => {
		if (tokenLookupCache.get(cacheKey) === tokenLookup) {
			tokenLookupCache.delete(cacheKey);
		}
	}, tokenLookupCacheTtlMs);

	const token = await promise;

	if (!token?.accessToken) {
		tokenLookupCache.delete(cacheKey);
	}

	return token;
}
