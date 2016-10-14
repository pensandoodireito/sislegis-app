--changeset coutinho:161021-1
CREATE TABLE proposicao_briefing (
  id                BIGINT PRIMARY KEY NOT NULL,
  datacriacao       TIMESTAMP NOT NULL,  
  proposicao_id     BIGINT NOT NULL,
  usuario_id        BIGINT NOT NULL,  
  documento_id        BIGINT ,
  url_arquivo		VARCHAR(256),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id),
  FOREIGN KEY (documento_id) REFERENCES documento (id),
  FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
--rollback drop table proposicao_briefing

--changeset coutinho:161021-2
CREATE TABLE proposicao_emenda (
  id                BIGINT PRIMARY KEY NOT NULL,
  datacriacao       TIMESTAMP NOT NULL,  
  proposicao_id     BIGINT NOT NULL,
  usuario_id        BIGINT NOT NULL,  
  documento_id        BIGINT ,
  url_arquivo		VARCHAR(256),
  FOREIGN KEY (proposicao_id) REFERENCES proposicao (id),
  FOREIGN KEY (documento_id) REFERENCES documento (id),
  FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
--rollback drop table proposicao_emenda



	
--changeset coutinho:161021-3
alter table areamerito_revisao ADD documento_id bigint;	
--rollback alter table areamerito_revisao drop documento_id

--changeset coutinho:161021-4
ALTER TABLE areamerito_revisao ADD CONSTRAINT fk_rev_doc FOREIGN KEY (documento_id)
	REFERENCES documento (id);
--rollback ALTER TABLE areamerito_revisao drop CONSTRAINT fk_rev_doc