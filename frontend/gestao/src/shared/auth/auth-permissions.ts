import type {AuthenticatedContext, AuthPermissionOptions,} from '@shared/auth/auth-token.types';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';

export function hasRole(
	authContext: Pick<AuthenticatedContext, 'applicationType' | 'roles'>,
	options: AuthPermissionOptions = {},
) {
	return (
		hasApplicationType(authContext, options.applicationTypes) &&
		hasAnyRequiredRole(authContext.roles, options.roles)
	);
}

export function hasApplicationType(
	authContext: Pick<AuthenticatedContext, 'applicationType'>,
	applicationTypes?: AuthPermissionOptions['applicationTypes'],
) {
	if (!applicationTypes || applicationTypes.length === 0) {
		return true;
	}

	if (!authContext.applicationType) {
		return false;
	}

	return applicationTypes.includes(authContext.applicationType);
}

/** Super user da plataforma (realm role admin-sistemas / flag Usuário principal). */
export function isPlatformAdmin(
	authContext: Pick<AuthenticatedContext, 'roles'> | readonly string[],
) {
	const roles = Array.isArray(authContext)
		? authContext
		: authContext.roles;

	return roles.includes(SISTEMAS.ADMIN);
}

function hasAnyRequiredRole(
	userRoles: readonly string[],
	requiredRoles?: readonly string[],
) {
	if (!requiredRoles || requiredRoles.length === 0) {
		return true;
	}

	if (isPlatformAdmin(userRoles)) {
		return true;
	}

	return requiredRoles.some((role) => userRoles.includes(role));
}
