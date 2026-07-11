import {redirect} from '@tanstack/react-router';

import {getAuthenticatedContext} from '@shared/auth/auth.server-fn';
import {hasRole} from '@shared/auth/auth-permissions';

import type {AuthPermissionOptions} from '@shared/auth/auth-token.types';

type RouteLocation = {
	href: string;
};

export async function routeRequiredAuthenticated({
	location,
}: {
	location: RouteLocation;
}) {
	const authContext = await getAuthenticatedContext();

	if (!authContext) {
		throw redirect({
			to: '/login',
			search: {
				redirect: location.href,
			},
		});
	}

	return authContext;
}

export async function routeRequiredUnauthenticated() {
	const authContext = await getAuthenticatedContext();

	if (authContext) {
		throw redirect({ to: '/' });
	}
}

export async function routeRequiredRoles({
	location,
	...options
}: AuthPermissionOptions & {
	location: RouteLocation;
}) {
	const authContext = await routeRequiredAuthenticated({ location });

	if (!hasRole(authContext, options)) {
		throw redirect({ to: '/unauthorized' });
	}

	return authContext;
}
