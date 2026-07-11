import {createFileRoute, Outlet, redirect} from '@tanstack/react-router';
import {AppShell} from '@components/widgets/app-shell';
import {authService} from '@entities/auth';
import {getMenus} from '@entities/menu';
import {routeRequiredAuthenticated} from '@shared/auth/auth-guards.server-fn';
import {invalidateAuthSession} from '@shared/auth/auth.server-fn';
import {ApiHttpError} from '@shared/api/api-error.type.ts';

export const Route = createFileRoute('/(authenticated)')({
	beforeLoad: async ({ location }) => {
		const authContext = await routeRequiredAuthenticated({ location });

		return {
			authContext,
		};
	},
	loader: async ({ context, location }) => {
		const authContext = context.authContext;
		const [usuario, grupo, menus] = await Promise.all([
			getAuthenticatedUserOrRedirect(location.href),
			authService.findGrupo(),
			getMenus(),
		]);

		return {
			authContext,
			menus,
			usuario: {
				...usuario,
				grupo: grupo ?? undefined,
			},
		};
	},
	component: RouteComponent,
});

function RouteComponent() {
	return (
		<AppShell>
			<Outlet />
		</AppShell>
	);
}

async function getAuthenticatedUserOrRedirect(redirectUrl: string) {
	try {
		return await authService.me();
	} catch (error) {
		if (isInvalidBackendCredential(error)) {
			await invalidateAuthSession();
			throw redirect({
				to: '/login',
				search: {
					redirect: redirectUrl,
				},
			});
		}

		throw error;
	}
}

function isInvalidBackendCredential(error: unknown) {
	return (
		error instanceof ApiHttpError &&
		(error.status === 401 || error.status === 403 || error.status === 503)
	);
}
