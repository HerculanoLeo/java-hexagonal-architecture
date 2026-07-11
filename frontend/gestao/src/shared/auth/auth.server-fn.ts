import {createServerFn} from '@tanstack/react-start';
import {z} from 'zod';

export const getAuthSession = createServerFn({ method: 'GET' }).handler(
	async () => {
		const { resolveAuthSession } =
			await import('@shared/auth/auth.session.server');

		return resolveAuthSession();
	},
);

export const getAuthenticatedContext = createServerFn({
	method: 'GET',
}).handler(async () => {
	const { resolveAuthenticatedContext } =
		await import('@shared/auth/auth.session.server');

	return resolveAuthenticatedContext();
});

export const requireAuthSession = createServerFn({ method: 'GET' }).handler(
	async () => {
		const { requireAuthSessionValue } =
			await import('@shared/auth/auth.session.server');

		return requireAuthSessionValue();
	},
);

const signOutSchema = z.object({
	postLogoutRedirectUri: z.url().optional(),
});

export const signOutAuthSession = createServerFn({
	method: 'POST',
})
	.validator(signOutSchema)
	.handler(async ({ data }) => {
		const { signOutAuthSessionValue } =
			await import('@shared/auth/auth.session.server');

		return signOutAuthSessionValue(data);
	});

export const invalidateAuthSession = createServerFn({
	method: 'POST',
}).handler(async () => {
	const { invalidateAuthSessionValue } =
		await import('@shared/auth/auth.session.server');

	return invalidateAuthSessionValue();
});

export const requireAuthenticatedContext = createServerFn({
	method: 'GET',
}).handler(async () => {
	const { requireAuthenticatedContextValue } =
		await import('@shared/auth/auth.session.server');

	return requireAuthenticatedContextValue();
});
