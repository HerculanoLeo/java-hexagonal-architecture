import {getRouteApi} from '@tanstack/react-router';

import {hasRole} from '@shared/auth/auth-permissions';

import type {AuthPermissionOptions} from '@shared/auth/auth-token.types';
import type {ReactNode} from 'react';

type GuardComponentProps = AuthPermissionOptions & {
	children: ReactNode;
	fallback?: ReactNode;
};

const authenticatedRoute = getRouteApi('/(authenticated)');

export default function GuardComponent({
	children,
	fallback = null,
	...options
}: GuardComponentProps) {
	const { authContext } = authenticatedRoute.useLoaderData();

	if (!hasRole(authContext, options)) {
		return fallback;
	}

	return children;
}
