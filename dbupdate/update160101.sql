--liquibase formatted sql

--changeset delgado:160101-1
CREATE TABLE processo_sei
(
  id BIGINT PRIMARY KEY NOT NULL,
  proposicao_id BIGINT NOT NULL,
  linksei VARCHAR(300),
  protocolo VARCHAR(50) NOT NULL,
  CONSTRAINT processo_sei_proposicao_id_fk FOREIGN KEY (proposicao_id) REFERENCES proposicao (id)
);
--rollback drop table processo_sei
CREATE UNIQUE INDEX processo_sei_proposicao_id_protocolo_uindex ON processo_sei (proposicao_id, linksei);
--rollback DROP INDEX public.processo_sei_proposicao_id_protocolo_uindex CASCADE
