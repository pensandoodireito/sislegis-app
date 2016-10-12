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