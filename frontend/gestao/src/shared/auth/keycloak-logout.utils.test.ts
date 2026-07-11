import {buildKeycloakLogoutUrl, resolvePostLogoutRedirectUri,} from './keycloak-logout.utils';

describe('buildKeycloakLogoutUrl', () => {
	it('builds a logout URL with required query params', () => {
		const url = new URL(
			buildKeycloakLogoutUrl({
				issuer: 'https://auth.example.com/realms/starter/',
				clientId: 'gestao',
				postLogoutRedirectUri: 'https://app.example.com/login',
			}),
		);

		expect(url.pathname).toBe('/realms/starter/protocol/openid-connect/logout');
		expect(url.searchParams.get('client_id')).toBe('gestao');
		expect(url.searchParams.get('post_logout_redirect_uri')).toBe(
			'https://app.example.com/login',
		);
		expect(url.searchParams.get('id_token_hint')).toBeNull();
	});

	it('includes id_token_hint when provided', () => {
		const url = new URL(
			buildKeycloakLogoutUrl({
				issuer: 'https://auth.example.com/realms/starter',
				clientId: 'gestao',
				postLogoutRedirectUri: 'https://app.example.com/login',
				idTokenHint: 'token-123',
			}),
		);

		expect(url.searchParams.get('id_token_hint')).toBe('token-123');
	});
});

describe('resolvePostLogoutRedirectUri', () => {
	const fallbackUri = 'https://app.example.com/login';
	const trustedOrigins = ['https://app.example.com'];

	it('returns the fallback when requested URI is missing', () => {
		expect(resolvePostLogoutRedirectUri(undefined, fallbackUri, trustedOrigins)).toBe(
			fallbackUri,
		);
	});

	it('returns the requested URI when origin is trusted', () => {
		expect(
			resolvePostLogoutRedirectUri(
				'https://app.example.com/custom-logout',
				fallbackUri,
				trustedOrigins,
			),
		).toBe('https://app.example.com/custom-logout');
	});

	it('falls back for untrusted or invalid URIs', () => {
		expect(
			resolvePostLogoutRedirectUri(
				'https://evil.example.com/logout',
				fallbackUri,
				trustedOrigins,
			),
		).toBe(fallbackUri);

		expect(
			resolvePostLogoutRedirectUri('not-a-url', fallbackUri, trustedOrigins),
		).toBe(fallbackUri);
	});
});
