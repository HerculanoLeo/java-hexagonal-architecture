CREATE SCHEMA IF NOT EXISTS security;

GRANT
USAGE
ON
SCHEMA
security TO
${app_role};
ALTER
DEFAULT PRIVILEGES IN SCHEMA security
    GRANT
SELECT,
INSERT
,
UPDATE,
DELETE
ON TABLES TO ${app_role};
ALTER
DEFAULT PRIVILEGES IN SCHEMA security
    GRANT USAGE,
SELECT
ON SEQUENCES TO ${app_role};
ALTER
DEFAULT PRIVILEGES IN SCHEMA security
    GRANT EXECUTE ON FUNCTIONS TO
${app_role};

CREATE TABLE IF NOT EXISTS security.tb_historico_login
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
    id_identity VARCHAR
(
    36
) NOT NULL,
    id_usuario UUID NULL,
    tipo CHAR
(
    2
) NOT NULL,
    id_relacionado VARCHAR
(
    36
) NULL,
    email VARCHAR
(
    255
) NULL,
    nome VARCHAR
(
    255
) NULL,
    ip VARCHAR
(
    64
) NULL,
    user_agent VARCHAR
(
    512
) NULL,
    sucesso BOOLEAN NOT NULL DEFAULT TRUE,
    dt_evento TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            id_sessao_bff VARCHAR (64) NULL
    );

CREATE UNIQUE INDEX IF NOT EXISTS uk_historico_login_sessao_bff
    ON security.tb_historico_login (id_sessao_bff)
    WHERE id_sessao_bff IS NOT NULL;

CREATE INDEX IF NOT EXISTS ix_historico_login_dt_evento
    ON security.tb_historico_login (dt_evento DESC);

CREATE INDEX IF NOT EXISTS ix_historico_login_tipo_relacionado_dt
    ON security.tb_historico_login (tipo, id_relacionado, dt_evento DESC);

CREATE INDEX IF NOT EXISTS ix_historico_login_email
    ON security.tb_historico_login (email);

CREATE INDEX IF NOT EXISTS ix_historico_login_identity
    ON security.tb_historico_login (id_identity);

GRANT
SELECT,
INSERT
,
UPDATE,
DELETE
ON ALL TABLES IN SCHEMA security TO ${app_role};
