import {
    decodeJwtClaims,
    getAuthzCookieExpiration,
    getTokenApplicationType,
    getTokenGroups,
    getTokenPermissions,
    getTokenRoles,
} from './auth-token.utils';

function createJwt(payload: Record<string, unknown>) {
	const header = Buffer.from(JSON.stringify({ alg: 'HS256', typ: 'JWT' })).toString(
		'base64url',
	);
	const body = Buffer.from(JSON.stringify(payload)).toString('base64url');

	return `${header}.${body}.signature`;
}

describe('decodeJwtClaims', () => {
	it('returns undefined for missing or malformed tokens', () => {
		expect(decodeJwtClaims()).toBeUndefined();
		expect(decodeJwtClaims('invalid-token')).toBeUndefined();
	});

	it('decodes a valid JWT payload', () => {
		const claims = decodeJwtClaims(
			createJwt({
				realm_access: { roles: ['admin'] },
				groups: ['/grupo-a'],
				application: { type: 'ST' },
				exp: 1_900_000_000,
			}),
		);

		expect(claims?.realm_access?.roles).toEqual(['admin']);
		expect(claims?.groups).toEqual(['/grupo-a']);
		expect(claims?.application?.type).toBe('ST');
	});
});

describe('getTokenRoles', () => {
	it('returns unique trimmed roles', () => {
		expect(
			getTokenRoles({
				realm_access: { roles: [' admin ', 'admin', ''] },
			}),
		).toEqual(['admin']);
	});
});

describe('getTokenGroups', () => {
	it('returns unique trimmed groups', () => {
		expect(
			getTokenGroups({
				groups: [' /grupo-a ', '/grupo-a'],
			}),
		).toEqual(['/grupo-a']);
	});
});

describe('getTokenPermissions', () => {
	it('merges roles and groups without duplicates', () => {
		expect(
			getTokenPermissions({
				realm_access: { roles: ['admin'] },
				groups: ['/grupo-a', 'admin'],
			}),
		).toEqual(['admin', '/grupo-a']);
	});
});

describe('getTokenApplicationType', () => {
	it('returns the application type from claims', () => {
		expect(
			getTokenApplicationType({
				application: { type: 'ST' },
			}),
		).toBe('ST');
	});
});

describe('getAuthzCookieExpiration', () => {
	const now = new Date('2026-01-01T00:00:00.000Z');

	it('uses token expiration minus safety window', () => {
		const result = getAuthzCookieExpiration({
			claims: { exp: Math.floor(now.getTime() / 1000) + 3600 },
			fallbackMaxAgeSeconds: 60,
			now,
			safetyWindowSeconds: 300,
		});

		expect(result?.maxAge).toBe(3300);
	});

	it('falls back to max age when token has no exp', () => {
		const result = getAuthzCookieExpiration({
			fallbackMaxAgeSeconds: 900,
			now,
			safetyWindowSeconds: 300,
		});

		expect(result?.maxAge).toBe(900);
	});

	it('returns undefined when expiration is already in the past', () => {
		const result = getAuthzCookieExpiration({
			claims: { exp: Math.floor(now.getTime() / 1000) - 10 },
			fallbackMaxAgeSeconds: 900,
			now,
			safetyWindowSeconds: 300,
		});

		expect(result).toBeUndefined();
	});
});
