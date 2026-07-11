import {betterAuth} from 'better-auth';
import {prismaAdapter} from 'better-auth/adapters/prisma';
import {genericOAuth, keycloak} from 'better-auth/plugins';
import {tanstackStartCookies} from 'better-auth/tanstack-start';

import {env} from '@/env';
import {prismaClient} from '@shared/database/prisma-client';

export const auth = betterAuth({
	baseURL: env.BETTER_AUTH_URL,
	secret: env.BETTER_AUTH_SECRET,
	session: {
		cookieCache: {
			enabled: true,
			maxAge: 5 * 60,
		},
	},
	...(prismaClient
		? {
				database: prismaAdapter(prismaClient, {
					provider: 'postgresql',
				}),
			}
		: {}),
	databaseHooks: {
		session: {
			create: {
				after: async (session) => {
					try {
						const { getKeycloakAccessToken } =
							await import('@shared/auth/auth-token.server');
						const { registerHistoricoLogin } =
							await import('@entities/historico-login/api/historico-login.register');

						const token = await getKeycloakAccessToken(session.userId);
						const accessToken = token?.accessToken;

						if (!accessToken) {
							return;
						}

						let email: string | undefined;
						let nome: string | undefined;

						if (prismaClient) {
							const user = await prismaClient.user.findUnique({
								where: { id: session.userId },
								select: { email: true, name: true },
							});
							email = user?.email;
							nome = user?.name ?? undefined;
						}

						await registerHistoricoLogin(accessToken, {
							ip: session.ipAddress ?? null,
							userAgent: session.userAgent ?? null,
							sessaoBffId: session.id,
							email: email ?? null,
							nome: nome ?? null,
						});
					} catch (error) {
						console.error(
							'[historico-login] falha ao registrar login (não bloqueia sessão)',
							error,
						);
					}
				},
			},
		},
	},
	plugins: [
		genericOAuth({
			config: [
				keycloak({
					clientId: env.KEYCLOAK_CLIENT_ID,
					clientSecret: env.KEYCLOAK_CLIENT_SECRET ?? '',
					issuer: env.KEYCLOAK_ISSUER,
					scopes: ['openid', 'profile', 'email'],
				}),
			],
		}),
		tanstackStartCookies(),
	],
});

export type AuthSession = typeof auth.$Infer.Session;
