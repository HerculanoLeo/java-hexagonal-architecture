CREATE SEQUENCE IF NOT EXISTS localizacao.sq_estado;
CREATE SEQUENCE IF NOT EXISTS localizacao.sq_municipio;

CREATE TABLE IF NOT EXISTS localizacao.tb_estado
(
    id     BIGINT      PRIMARY KEY NOT NULL,
    sigla  VARCHAR(2)              NOT NULL,
    nome   VARCHAR(100)            NOT NULL,
    status CHAR                    NOT NULL
);

CREATE TABLE IF NOT EXISTS localizacao.tb_municipio
(
    id        BIGINT       PRIMARY KEY NOT NULL,
    nome      VARCHAR(100)             NOT NULL,
    id_estado BIGINT                   NOT NULL,
    status    CHAR                     NOT NULL,
    CONSTRAINT fk_municipio_estado FOREIGN KEY (id_estado) REFERENCES localizacao.tb_estado
);

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA localizacao TO ${app_role};
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA localizacao TO ${app_role};
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA localizacao TO ${app_role};
