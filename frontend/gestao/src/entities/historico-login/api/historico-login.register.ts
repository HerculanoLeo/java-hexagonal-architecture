import type {HistoricoLoginRegisterRequest} from '../model/historico-login-register.schema';

/**
 * Registra login bem-sucedido no backend Java.
 * Usa token Keycloak do usuário; falhas não devem bloquear o login.
 */
export async function registerHistoricoLogin(
	accessToken: string,
	payload: HistoricoLoginRegisterRequest,
): Promise<void> {
	const { env } = await import('#/env.ts');

	const normalizedBaseUrl = env.CADASTROS_URL.replace(/\/+$/, '');
	const url = `${normalizedBaseUrl}/seguranca/historico-logins`;

	const response = await fetch(url, {
		method: 'POST',
		headers: {
			authorization: `Bearer ${accessToken}`,
			'content-type': 'application/json',
		},
		body: JSON.stringify(payload),
	});

	if (!response.ok && response.status !== 202) {
		throw new Error(
			`Falha ao registrar histórico de login (HTTP ${response.status})`,
		);
	}
}
