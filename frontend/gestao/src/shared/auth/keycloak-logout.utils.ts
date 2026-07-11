type BuildKeycloakLogoutUrlOptions = {
	clientId: string;
	idTokenHint?: string;
	issuer: string;
	postLogoutRedirectUri: string;
};

export function buildKeycloakLogoutUrl({
	clientId,
	idTokenHint,
	issuer,
	postLogoutRedirectUri,
}: BuildKeycloakLogoutUrlOptions) {
	const normalizedIssuer = issuer.replace(/\/+$/, '');
	const url = new URL(`${normalizedIssuer}/protocol/openid-connect/logout`);

	url.searchParams.set('client_id', clientId);
	url.searchParams.set('post_logout_redirect_uri', postLogoutRedirectUri);

	if (idTokenHint) {
		url.searchParams.set('id_token_hint', idTokenHint);
	}

	return url.toString();
}

export function resolvePostLogoutRedirectUri(
	requestedUri: string | undefined,
	fallbackUri: string,
	trustedOrigins: string[],
) {
	if (!requestedUri) {
		return fallbackUri;
	}

	try {
		const requestedUrl = new URL(requestedUri);
		const isTrustedOrigin = trustedOrigins.some((origin) => {
			return requestedUrl.origin === new URL(origin).origin;
		});

		if (isTrustedOrigin) {
			return requestedUrl.toString();
		}
	} catch {
		return fallbackUri;
	}

	return fallbackUri;
}
