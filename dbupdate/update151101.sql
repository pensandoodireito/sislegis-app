--liquibase formatted sql
--changeset release151101:151101-8
CREATE TABLE posicionamento_proposicao (
  id                BIGINT PRIMARY KEY NOT NULL,
  datacriacao       TIMESTAMP,
  posicionamento_id BIGINT,
  proposicao_id     BIGINT,
  usuario_id        BIGINT,
  FOREIGN KEY (posicionamento_id) REFERENCES posicionamento (id),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id),
  FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);