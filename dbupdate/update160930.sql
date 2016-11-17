

--liquibase formatted sql

--changeset coutinho:160930-1
alter table proposicao ADD posicionamento_supar_id bigint;	
--rollback alter table proposicao drop posicionamento_supar_id

--changeset coutinho:160930-2
ALTER TABLE proposicao ADD CONSTRAINT fk_supar_posicao FOREIGN KEY (posicionamento_supar_id)
	REFERENCES posicionamento (id);
--rollback ALTER TABLE proposicao drop CONSTRAINT fk_supar_posicao

	
--changeset coutinho:160930-3
alter table proposicao ADD idequipe bigint;	
--rollback alter table proposicao drop idequipe

--changeset coutinho:160930-4
ALTER TABLE proposicao ADD CONSTRAINT fk_equipe FOREIGN KEY (idequipe)
	REFERENCES equipe (id);
--rollback ALTER TABLE proposicao drop CONSTRAINT fk_equipe

	
	
--changeset coutinho:160930-5
drop TABLE equipe_usuario;
--rollback
	
	
--changeset coutinho:160930-6
alter table usuario ADD idequipe bigint;	
--rollback alter table usuario drop idequipe

--changeset coutinho:160930-7
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_equipe FOREIGN KEY (idequipe)
	REFERENCES equipe (id);
--rollback ALTER TABLE proposicao drop CONSTRAINT fk_usuario_equipe

	
--changeset coutinho:160930-8
alter table proposicao ADD created timestamp not null;
--rollback ALTER TABLE proposicao drop created
--changeset coutinho:160930-9
alter table proposicao ADD updated timestamp not null;
--rollback ALTER TABLE proposicao drop created

--changeset coutinho:160930-10
alter table areamerito ADD contato_nome varchar(128);
--rollback ALTER TABLE areamerito drop contato_nome

--changeset coutinho:160930-11
alter table areamerito ADD contato_email varchar(128);
--rollback ALTER TABLE areamerito drop created

--changeset coutinho:160930-12
ALTER TABLE areamerito alter contato_id  DROP NOT NULL;
--rollback 




