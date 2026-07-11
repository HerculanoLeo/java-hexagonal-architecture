import type {AuthSession} from '@shared/auth/auth.configuration';

export type ApplicationType = 'ST' | (string & {});

export type KeycloakTokenClaims = {
	application?: {
		type?: string;
	};
	exp?: number;
	groups?: string[];
	realm_access?: {
		roles?: string[];
	};
	resource_access?: Record<
		string,
		{
			roles?: string[];
		}
	>;
};

export type AuthenticatedContext = {
	applicationType?: ApplicationType;
	groups: string[];
	roles: string[];
	session: AuthSession;
	tokenClaims?: KeycloakTokenClaims;
};

export type AuthPermissionOptions = {
	applicationTypes?: readonly ApplicationType[];
	roles?: readonly string[];
};

export type AuthzCookiePayload = {
	applicationType?: ApplicationType;
	exp: number;
	groups: string[];
	roles: string[];
	sessionId: string;
	userId: string;
};
