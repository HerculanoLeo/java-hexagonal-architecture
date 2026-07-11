import {revokeSessionSchema} from './revoke-session.schema';

describe('revokeSessionSchema', () => {
	it('accepts a non-empty session id', () => {
		expect(revokeSessionSchema.safeParse({ sessionId: 'session-1' }).success).toBe(
			true,
		);
	});

	it('rejects an empty session id', () => {
		expect(revokeSessionSchema.safeParse({ sessionId: '' }).success).toBe(false);
	});
});
