--liquibase formatted sql

--changeset delgado:151102-1
CREATE TABLE roadmap_comissao (
  proposicao_id bigint not null,
  comissao varchar(50) not null,
  ordem integer not null,
  PRIMARY KEY (proposicao_id, comissao, ordem),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id)
);
--rollback drop table roadmap_comissao

--changeset coutinho:151201-2
CREATE TABLE situacao_legislativa (
	id BIGINT NOT NULL PRIMARY KEY,
	descricao VARCHAR(128) not null,
	sigla VARCHAR(20),
	origem VARCHAR(50) not null,
	idExterno BIGINT not null,
	atualizada_em TIMESTAMP,
	atualizada_por BIGINT,	
	terminativa BOOLEAN DEFAULT FALSE NOT NULL,
	obsoleta BOOLEAN DEFAULT FALSE NOT NULL
);
--rollback drop table situacao_legislativa

--changeset coutinho:151201-3
ALTER TABLE situacao_legislativa ADD CONSTRAINT fk_sit_user FOREIGN KEY (atualizada_por) REFERENCES usuario (id);
--rollback ALTER TABLE situacao_legislativa drop CONSTRAINT fk_sit_user

--changeset coutinho:151201-4
CREATE UNIQUE INDEX uk_ln25mgw4he9yywe7ck4ua124b ON situacao_legislativa (origem ASC, idexterno ASC);
--rollback ALTER TABLE situacao_legislativa drop INDEX uk_ln25mgw4he9yywe7ck4ua124b
	

