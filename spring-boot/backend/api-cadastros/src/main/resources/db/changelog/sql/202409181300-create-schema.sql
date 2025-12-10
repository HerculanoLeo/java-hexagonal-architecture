CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;

CREATE TABLE IF NOT EXISTS event_publication
(
    id               UUID                     NOT NULL,
    completion_date  TIMESTAMP WITH TIME ZONE NULL,
    event_type       VARCHAR(255)             NOT NULL,
    listener_id      VARCHAR(255)             NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    serialized_event VARCHAR(4000)            NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_sistema_usuario
(
    id             UUID PRIMARY KEY         NOT NULL DEFAULT uuid_generate_v4(),
    id_identity    VARCHAR(36) UNIQUE       NOT NULL,
    email          VARCHAR(255) UNIQUE      NOT NULL,
    nome           VARCHAR(255)             NOT NULL,
    status         CHAR(1)                  NOT NULL,
    dt_criacao     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dt_atualizacao TIMESTAMP WITH TIME ZONE NULL,
    versao         INTEGER                  NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS tb_notificacao
(
    id             UUID PRIMARY KEY         NOT NULL DEFAULT uuid_generate_v4(),
    titulo         VARCHAR(100)             NOT NULL,
    conteudo       TEXT                     NOT NULL,
    destinatarios  TEXT                     NOT NULL,
    dt_solicitacao TIMESTAMP WITH TIME ZONE NOT NULL,
    dt_envio       TIMESTAMP WITH TIME ZONE NULL,
    status         CHAR(1)                  NOT NULL,
    tipo           CHAR(2)                  NOT NULL,
    tentativas     INTEGER                  NOT NULL DEFAULT 0,
    versao         INTEGER                  NOT NULL DEFAULT 0,
    dt_criacao     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dt_atualizacao TIMESTAMP WITH TIME ZONE NULL,
    metadados      JSONB                    NULL,
    errors         JSONB                    NULL
);

CREATE TABLE IF NOT EXISTS tb_notificacao_configuracao
(
    id     UUID PRIMARY KEY   NOT NULL DEFAULT uuid_generate_v4(),
    codigo VARCHAR(60) UNIQUE NOT NULL,
    valor  TEXT               NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS sq_estado;

CREATE SEQUENCE IF NOT EXISTS sq_municipio;

CREATE TABLE IF NOT EXISTS tb_estado
(
    id     BIGINT PRIMARY KEY NOT NULL,
    sigla  VARCHAR(2)         NOT NULL,
    nome   VARCHAR(100)       NOT NULL,
    status CHAR               NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_municipio
(
    id        BIGINT PRIMARY KEY NOT NULL,
    nome      VARCHAR(100)       NOT NULL,
    id_estado BIGINT NOT NULL,
    status    CHAR   NOT NULL,
    CONSTRAINT fk_municipio_estado FOREIGN KEY (id_estado)
        REFERENCES tb_estado
);

-- DADOS --
INSERT INTO tb_notificacao_configuracao(codigo, valor)
VALUES ('boas_vindas.titulo', '[PORTAL] Seja bem vindo(a) ao nosso portal');
INSERT INTO tb_notificacao_configuracao(codigo, valor)
VALUES ('troca_senha.titulo', '[PORTAL] Troca de senha');
