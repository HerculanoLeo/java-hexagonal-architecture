import {revokeAllSessionsSchema} from './revoke-all-sessions.schema';

describe('revokeAllSessionsSchema', () => {
	it('accepts a non-empty user id', () => {
		expect(revokeAllSessionsSchema.safeParse({ userId: 'user-1' }).success).toBe(
			true,
		);
	});

	it('rejects an empty user id', () => {
		expect(revokeAllSessionsSchema.safeParse({ userId: '' }).success).toBe(false);
	});
});
