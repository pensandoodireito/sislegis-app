--liquibase formatted sql

--changeset delgado:160101-1
CREATE TABLE processo_sei
(
  id BIGINT PRIMARY KEY NOT NULL,
  proposicao_id BIGINT NOT NULL,
  nup VARCHAR(50) NOT NULL,
  linksei VARCHAR(300),
  CONSTRAINT processo_sei_proposicao_id_fk FOREIGN KEY (proposicao_id) REFERENCES proposicao (id)
);
--rollback drop table processo_sei
