import {createAuthClient} from 'better-auth/react';
import {genericOAuthClient} from 'better-auth/client/plugins';

import {signOutAuthSession} from '@shared/auth/auth.server-fn';

export const authClient = createAuthClient({
	plugins: [genericOAuthClient()],
});

export async function signOutWithKeycloak(
	postLogoutRedirectUri = `${window.location.origin}/login`,
) {
	const { redirectUrl } = await signOutAuthSession({
		data: {
			postLogoutRedirectUri,
		},
	});

	window.location.assign(redirectUrl);
}
