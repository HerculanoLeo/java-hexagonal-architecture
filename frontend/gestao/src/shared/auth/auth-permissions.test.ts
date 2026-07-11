import {hasApplicationType, hasRole, isPlatformAdmin} from './auth-permissions';

const authContext = {
	applicationType: 'ST' as const,
	roles: ['usuarios-sistemas', 'grupos-sistemas'],
};

describe('hasApplicationType', () => {
	it('allows any application type when none are required', () => {
		expect(hasApplicationType(authContext)).toBe(true);
	});

	it('matches when the user application type is listed', () => {
		expect(hasApplicationType(authContext, ['ST'])).toBe(true);
	});

	it('rejects when application type is missing or not listed', () => {
		expect(hasApplicationType({ applicationType: undefined }, ['ST'])).toBe(
			false,
		);
		expect(hasApplicationType(authContext, ['OTHER'])).toBe(false);
	});
});

describe('isPlatformAdmin', () => {
	it('detects admin-sistemas on roles array or auth context', () => {
		expect(isPlatformAdmin(['admin-sistemas'])).toBe(true);
		expect(
			isPlatformAdmin({
				roles: ['admin-sistemas'],
			}),
		).toBe(true);
		expect(isPlatformAdmin(authContext)).toBe(false);
	});
});

describe('hasRole', () => {
	it('allows access when no roles are required', () => {
		expect(hasRole(authContext)).toBe(true);
	});

	it('requires at least one matching role', () => {
		expect(hasRole(authContext, { roles: ['usuarios-sistemas'] })).toBe(true);
		expect(hasRole(authContext, { roles: ['missing-role'] })).toBe(false);
	});

	it('grants any role when user has admin-sistemas', () => {
		const mainUser = {
			applicationType: 'ST' as const,
			roles: ['admin-sistemas'],
		};

		expect(hasRole(mainUser, { roles: ['usuarios-sistemas'] })).toBe(true);
		expect(hasRole(mainUser, { roles: ['missing-role'] })).toBe(true);
	});

	it('combines application type and role checks', () => {
		expect(
			hasRole(authContext, {
				applicationTypes: ['ST'],
				roles: ['grupos-sistemas'],
			}),
		).toBe(true);

		expect(
			hasRole(authContext, {
				applicationTypes: ['OTHER'],
				roles: ['grupos-sistemas'],
			}),
		).toBe(false);
	});
});
