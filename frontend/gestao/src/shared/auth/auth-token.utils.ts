import type {KeycloakTokenClaims} from '@shared/auth/auth-token.types';

export function decodeJwtClaims(token?: string | null) {
	if (!token) {
		return undefined;
	}

	const [, payload] = token.split('.');

	if (!payload) {
		return undefined;
	}

	try {
		const json = Buffer.from(payload, 'base64url').toString('utf-8');

		return JSON.parse(json) as KeycloakTokenClaims;
	} catch {
		return undefined;
	}
}

export function getTokenRoles(claims?: KeycloakTokenClaims) {
	return uniqueStrings([...(claims?.realm_access?.roles ?? [])]);
}

export function getTokenGroups(claims?: KeycloakTokenClaims) {
	return uniqueStrings([...(claims?.groups ?? [])]);
}

export function getTokenPermissions(claims?: KeycloakTokenClaims) {
	return uniqueStrings([...getTokenRoles(claims), ...getTokenGroups(claims)]);
}

export function getTokenApplicationType(claims?: KeycloakTokenClaims) {
	return claims?.application?.type;
}

export function getAuthzCookieExpiration({
	claims,
	fallbackMaxAgeSeconds,
	now = new Date(),
	safetyWindowSeconds,
}: {
	claims?: KeycloakTokenClaims;
	fallbackMaxAgeSeconds: number;
	now?: Date;
	safetyWindowSeconds: number;
}) {
	const nowSeconds = Math.floor(now.getTime() / 1000);
	const expiresAt = claims?.exp
		? claims.exp - safetyWindowSeconds
		: nowSeconds + fallbackMaxAgeSeconds;

	if (expiresAt <= nowSeconds) {
		return undefined;
	}

	return {
		expiresAt,
		maxAge: expiresAt - nowSeconds,
	};
}

function uniqueStrings(values: string[]) {
	return Array.from(
		new Set(
			values.map((value) => value.trim()).filter((value) => value.length > 0),
		),
	);
}
