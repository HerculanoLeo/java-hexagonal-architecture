import {createFileRoute} from '@tanstack/react-router';

import {LoginPage} from '@components/pages/auth/LoginPage';
import {routeRequiredUnauthenticated} from '@shared/auth/auth-guards.server-fn';

type LoginSearch = {
	redirect?: string;
};

export const Route = createFileRoute('/(unauthenticated)/login')({
	validateSearch: (search: Record<string, unknown>): LoginSearch => ({
		redirect: typeof search.redirect === 'string' ? search.redirect : undefined,
	}),
	beforeLoad: routeRequiredUnauthenticated,
	component: LoginPage,
});
