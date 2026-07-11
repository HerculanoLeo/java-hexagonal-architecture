import {getRequestHeaders} from '@tanstack/react-start/server';

import {env} from '@/env';
import {auth} from '@shared/auth/auth.configuration';
import {getKeycloakAccessToken, getKeycloakIdTokenHint, getSession,} from '@shared/auth/auth-token.server';
import {buildKeycloakLogoutUrl, resolvePostLogoutRedirectUri,} from '@shared/auth/keycloak-logout.utils';
import {clearAuthzCookie, getAuthzCookie, setAuthzCookie,} from '@shared/auth/authz-cookie.server';
import {
    decodeJwtClaims,
    getAuthzCookieExpiration,
    getTokenApplicationType,
    getTokenGroups,
    getTokenPermissions,
} from '@shared/auth/auth-token.utils';

import type {AuthenticatedContext} from '@shared/auth/auth-token.types';

export async function resolveAuthSession() {
	const headers = getRequestHeaders();
	return getSession(headers);
}

export async function resolveAuthenticatedContext(): Promise<AuthenticatedContext | null> {
	const session = await resolveAuthSession();

	if (!session) {
		clearAuthzCookie();
		return null;
	}

	const cachedAuthContext = getAuthenticatedContextFromCookie(session);

	if (cachedAuthContext) {
		return cachedAuthContext;
	}

	const token = await getKeycloakAccessToken(session.user.id);
	const tokenClaims =
		decodeJwtClaims(token?.accessToken) ?? decodeJwtClaims(token?.idToken);
	const authContext = {
		applicationType: getTokenApplicationType(tokenClaims),
		groups: getTokenGroups(tokenClaims),
		roles: getTokenPermissions(tokenClaims),
		session,
		tokenClaims,
	};

	const expiration = getAuthzCookieExpiration({
		claims: tokenClaims,
		fallbackMaxAgeSeconds: env.AUTHZ_COOKIE_FALLBACK_MAX_AGE_SECONDS,
		safetyWindowSeconds: env.AUTHZ_COOKIE_SAFETY_WINDOW_SECONDS,
	});

	if (expiration) {
		setAuthzCookie({
			applicationType: authContext.applicationType,
			exp: expiration.expiresAt,
			groups: authContext.groups,
			roles: authContext.roles,
			sessionId: session.session.id,
			userId: session.user.id,
		});
	}

	return authContext;
}

function getAuthenticatedContextFromCookie(
	session: AuthenticatedContext['session'],
) {
	const authzCookie = getAuthzCookie();

	if (!authzCookie) {
		return undefined;
	}

	if (
		authzCookie.userId !== session.user.id ||
		authzCookie.sessionId !== session.session.id
	) {
		clearAuthzCookie();
		return undefined;
	}

	return {
		applicationType: authzCookie.applicationType,
		groups: authzCookie.groups,
		roles: authzCookie.roles,
		session,
	};
}

export async function requireAuthSessionValue() {
	const session = await resolveAuthSession();

	if (!session) {
		throw new Error('Unauthorized');
	}

	return session;
}

export async function requireAuthenticatedContextValue(): Promise<AuthenticatedContext> {
	const authContext = await resolveAuthenticatedContext();

	if (!authContext) {
		throw new Error('Unauthorized');
	}

	return authContext;
}

type SignOutInput = {
	postLogoutRedirectUri?: string;
};

export async function signOutAuthSessionValue(data: SignOutInput) {
	const headers = getRequestHeaders();
	const session = await getSession(headers);
	const idTokenHint = session
		? await getKeycloakIdTokenHint(session.user.id)
		: undefined;
	const fallbackRedirectUri = new URL(
		'/login',
		env.BETTER_AUTH_URL ?? env.SERVER_URL ?? 'http://localhost:3000',
	).toString();
	const trustedOrigins = [env.BETTER_AUTH_URL, env.SERVER_URL].filter(
		(origin): origin is string => Boolean(origin),
	);
	const postLogoutRedirectUri = resolvePostLogoutRedirectUri(
		data.postLogoutRedirectUri,
		fallbackRedirectUri,
		trustedOrigins,
	);

	clearAuthzCookie();

	try {
		await auth.api.signOut({
			headers,
		});
	} catch {
		// Continue with provider logout even if local session cleanup fails.
	}

	return {
		redirectUrl: buildKeycloakLogoutUrl({
			clientId: env.KEYCLOAK_CLIENT_ID,
			idTokenHint,
			issuer: env.KEYCLOAK_ISSUER,
			postLogoutRedirectUri,
		}),
	};
}

export async function invalidateAuthSessionValue() {
	const headers = getRequestHeaders();

	clearAuthzCookie();

	try {
		await auth.api.signOut({
			headers,
		});
	} catch {
		// The frontend session is already considered invalid for this request.
	}
}
