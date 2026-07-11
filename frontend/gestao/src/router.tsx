import {createRouter as createTanStackRouter, Navigate,} from '@tanstack/react-router';
import {routeTree} from './routeTree.gen';

import {setupRouterSsrQueryIntegration} from '@tanstack/react-router-ssr-query';
import {getContext} from '@configurations/tanstack-query/root-provider';

export function getRouter() {
	const context = getContext();

	const router = createTanStackRouter({
		routeTree,
		context,
		scrollRestoration: true,
		defaultPreload: 'intent',
		defaultPreloadStaleTime: 0,
		defaultNotFoundComponent: () => <Navigate to={'/not-found'} />,
	});

	setupRouterSsrQueryIntegration({ router, queryClient: context.queryClient });

	return router;
}

declare module '@tanstack/react-router' {
	interface Register {
		router: ReturnType<typeof getRouter>;
	}
}
