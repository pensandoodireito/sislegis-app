--liquibase formatted sql
--changeset release151101:151101-8
CREATE TABLE posicionamento_proposicao (
  id                BIGINT PRIMARY KEY NOT NULL,
  datacriacao       TIMESTAMP NOT NULL,
  posicionamento_id BIGINT,
  proposicao_id     BIGINT NOT NULL,
  usuario_id        BIGINT NOT NULL,
  FOREIGN KEY (posicionamento_id) REFERENCES posicionamento (id),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id),
  FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

--changeset release151101:151101-9
ALTER TABLE public.posicionamento ADD preliminar BOOLEAN DEFAULT FALSE NOT NULL;
