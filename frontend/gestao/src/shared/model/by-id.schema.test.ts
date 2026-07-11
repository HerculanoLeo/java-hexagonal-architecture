import {byIdSchema} from './by-id.schema';

describe('byIdSchema', () => {
	it('accepts a non-empty id', () => {
		expect(byIdSchema.safeParse({ id: 'resource-1' }).success).toBe(true);
	});

	it('rejects an empty id', () => {
		expect(byIdSchema.safeParse({ id: '' }).success).toBe(false);
	});
});
