CREATE TABLE IF NOT EXISTS notification.tb_notificacao
(
    id
    UUID
    PRIMARY
    KEY
    NOT
    NULL
    DEFAULT
    uuidv7
(
),
    titulo VARCHAR
(
    100
) NOT NULL,
    conteudo TEXT NOT NULL,
    destinatarios TEXT NOT NULL,
    dt_solicitacao TIMESTAMP WITH TIME ZONE NOT NULL,
    dt_envio TIMESTAMP WITH TIME ZONE NULL,
                           status CHAR (1) NOT NULL,
    tipo CHAR
(
    2
) NOT NULL,
    tentativas INTEGER NOT NULL DEFAULT 0,
    versao INTEGER NOT NULL DEFAULT 0,
    dt_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dt_atualizacao TIMESTAMP
                       WITH TIME ZONE NULL,
                           metadados JSONB NULL,
                           errors JSONB NULL
                           );

CREATE TABLE IF NOT EXISTS notification.tb_notificacao_configuracao
(
    id
    UUID
    PRIMARY
    KEY
    NOT
    NULL
    DEFAULT
    uuidv7
(
),
    codigo VARCHAR
(
    60
) UNIQUE NOT NULL,
    valor TEXT NOT NULL
    );

INSERT INTO notification.tb_notificacao_configuracao(codigo, valor)
VALUES ('boas_vindas.titulo', '[PORTAL] Seja bem vindo(a) ao nosso portal');
INSERT INTO notification.tb_notificacao_configuracao(codigo, valor)
VALUES ('troca_senha.titulo', '[PORTAL] Troca de senha');
INSERT INTO notification.tb_notificacao_configuracao(codigo, valor)
VALUES ('usuario_ativado.titulo', '[PORTAL] Acesso reativado');
INSERT INTO notification.tb_notificacao_configuracao(codigo, valor)
VALUES ('usuario_inativado.titulo', '[PORTAL] Acesso inativado');
INSERT INTO notification.tb_notificacao_configuracao(codigo, valor)
VALUES ('teste.titulo', '[PORTAL] E-mail de teste');

GRANT
SELECT,
INSERT
,
UPDATE,
DELETE
ON ALL TABLES IN SCHEMA notification TO
${app_role};
GRANT
USAGE,
SELECT
ON ALL SEQUENCES IN SCHEMA notification TO ${app_role};
GRANT
EXECUTE
ON
ALL
FUNCTIONS IN SCHEMA notification TO
${app_role};
