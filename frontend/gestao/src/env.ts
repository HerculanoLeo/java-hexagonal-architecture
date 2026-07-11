import {createEnv} from '@t3-oss/env-core';
import {z} from 'zod';

const serverEnv =
	typeof process !== 'undefined' ? process.env : ({} as NodeJS.ProcessEnv);

export const env = createEnv({
	server: {
		NODE_ENV: z
			.enum(['development', 'production', 'test'])
			.default('development'),
		VERSION: z.string().min(1).default('0.0.0'),
		CADASTROS_URL: z.url(),
		DATABASE_URL: z.url().optional(),
		PRISMA_LOG_QUERIES: z
			.enum(['true', 'false'])
			.default('false')
			.transform((value) => value === 'true'),
		BETTER_AUTH_URL: z.url().optional(),
		BETTER_AUTH_SECRET: z.string().min(32).optional(),
		AUTHZ_COOKIE_NAME: z.string().min(1).default('starter.authz'),
		AUTHZ_COOKIE_FALLBACK_MAX_AGE_SECONDS: z.coerce
			.number()
			.int()
			.positive()
			.default(300),
		AUTHZ_COOKIE_SAFETY_WINDOW_SECONDS: z.coerce
			.number()
			.int()
			.nonnegative()
			.default(30),
		KEYCLOAK_CLIENT_ID: z.string().min(1),
		KEYCLOAK_CLIENT_SECRET: z.string().min(1).optional(),
		KEYCLOAK_ISSUER: z.url(),
		SERVER_URL: z.url().optional(),
	},

	/**
	 * The prefix that client-side variables must have. This is enforced both at
	 * a type-level and at runtime.
	 */
	clientPrefix: 'VITE_',

	client: {
		VITE_APP_TITLE: z.string().min(1).optional(),
	},

	/**
	 * What object holds the environment variables at runtime. This is usually
	 * `process.env` or `import.meta.env`.
	 */
	runtimeEnv: {
		...serverEnv,
		...import.meta.env,
	},

	/**
	 * By default, this library will feed the environment variables directly to
	 * the Zod validator.
	 *
	 * This means that if you have an empty string for a value that is supposed
	 * to be a number (e.g. `PORT=` in a ".env.local" file), Zod will incorrectly flag
	 * it as a type mismatch violation. Additionally, if you have an empty string
	 * for a value that is supposed to be a string with a default value (e.g.
	 * `DOMAIN=` in an ".env.local" file), the default value will never be applied.
	 *
	 * In order to solve these issues, we recommend that all new projects
	 * explicitly specify this option as true.
	 */
	emptyStringAsUndefined: true,
});

export function requireServerEnv() {
	const schema = z.object({
		DATABASE_URL: z.url(),
		BETTER_AUTH_URL: z.url(),
		BETTER_AUTH_SECRET: z.string().min(32),
		KEYCLOAK_CLIENT_ID: z.string().min(1),
		KEYCLOAK_CLIENT_SECRET: z.string().min(1),
		KEYCLOAK_ISSUER: z.url(),
		CADASTROS_URL: z.url(),
	});
	return schema.parse(serverEnv);
}
