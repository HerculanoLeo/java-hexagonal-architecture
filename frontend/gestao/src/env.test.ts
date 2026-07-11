import {requireServerEnv} from './env';

describe('requireServerEnv', () => {
	const requiredEnv = {
		DATABASE_URL: 'postgresql://localhost:5432/gestao',
		BETTER_AUTH_URL: 'http://localhost:3000',
		BETTER_AUTH_SECRET: 'a'.repeat(32),
		KEYCLOAK_CLIENT_ID: 'gestao',
		KEYCLOAK_CLIENT_SECRET: 'secret',
		KEYCLOAK_ISSUER: 'https://auth.example.com/realms/starter',
		CADASTROS_URL: 'http://localhost:8080',
	};

	beforeEach(() => {
		Object.assign(process.env, requiredEnv);
	});

	it('parses required server environment variables', () => {
		expect(requireServerEnv()).toEqual(requiredEnv);
	});

	it('throws when a required variable is missing', () => {
		delete process.env.DATABASE_URL;

		expect(() => requireServerEnv()).toThrow();
	});
});
