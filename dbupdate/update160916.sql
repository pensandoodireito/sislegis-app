

--liquibase formatted sql

--changeset coutinho:160916-1
alter table comissao ADD nome varchar(128);
--rollback alter table comissao drop nome

--changeset coutinho:160916-2
CREATE TABLE areamerito
(
  id BIGINT PRIMARY KEY NOT NULL,
  nome VARCHAR(128) NOT NULL,  
  contato_id bigint not null,
  CONSTRAINT usuario_id_fk FOREIGN KEY (contato_id) REFERENCES usuario (id)	
);
--rollback drop table area_merito

CREATE UNIQUE INDEX areamerito_nome_unico ON areamerito (nome);
--rollback DROP INDEX areamerito_nome_unico CASCADE

--changeset coutinho:160916-3
CREATE TABLE areamerito_revisao
(  
  id    BIGINT PRIMARY KEY NOT NULL,
  datacriacao       TIMESTAMP NOT NULL,
  dataatualizacao       TIMESTAMP NOT NULL,
  posicionamento_id BIGINT,
  areamerito_id BIGINT  NOT NULL,	
  proposicao_id     BIGINT NOT NULL,  
  FOREIGN KEY (posicionamento_id) REFERENCES posicionamento (id),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id),
  FOREIGN KEY (areamerito_id) REFERENCES areamerito (id),
  parecer VARCHAR(5000)    
);
--rollback drop table areamerito_revisao


