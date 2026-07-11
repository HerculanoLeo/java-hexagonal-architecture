export enum SISTEMAS {
	/** Super user da plataforma — atribuído direto no usuário Keycloak (flag Usuário principal). */
	ADMIN = 'admin-sistemas',
	USUARIOS = 'usuarios-sistemas',
	GRUPOS = 'grupos-sistemas',
	SEGURANCA_USUARIOS_ONLINE = 'seguranca-usuarios-ativos-sistemas',
	SEGURANCA_HISTORICO_LOGINS = 'seguranca-historico-login-sistemas',
	CONFIGURACAO = 'configuracao-sistemas',
	NOTIFICACAO = 'notificacao-sistemas',
}
