--liquibase formatted sql

--changeset release151102:151102-1
CREATE TABLE roadmap_comissao (
  proposicao_id bigint not null,
  comissao varchar(50) not null,
  ordem integer not null,
  PRIMARY KEY (proposicao_id, comissao, ordem),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id)
);
--rollback drop table roadmap_comissao
