CREATE TABLE IF NOT EXISTS backoffice.tb_sistema_usuario
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
) UNIQUE NOT NULL,
    email VARCHAR
(
    255
) UNIQUE NOT NULL,
    nome VARCHAR
(
    255
) NOT NULL,
    status CHAR
(
    1
) NOT NULL,
    main BOOLEAN NOT NULL DEFAULT FALSE,
    dt_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dt_atualizacao TIMESTAMP WITH TIME ZONE NULL,
                                 versao INTEGER NOT NULL DEFAULT 0
                                 );

GRANT
SELECT,
INSERT
,
UPDATE,
DELETE
ON ALL TABLES IN SCHEMA plataformadmin TO
${app_role};
GRANT
USAGE,
SELECT
ON ALL SEQUENCES IN SCHEMA plataformadmin TO ${app_role};
GRANT
EXECUTE
ON
ALL
FUNCTIONS IN SCHEMA plataformadmin TO
${app_role};
