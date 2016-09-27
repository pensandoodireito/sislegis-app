

--liquibase formatted sql

--changeset coutinho:160930-1
alter table proposicao ADD posicionamento_supar_id bigint;	
--rollback alter table proposicao drop posicionamento_supar_id

--changeset coutinho:160930-2
ALTER TABLE proposicao ADD CONSTRAINT fk_supar_posicao FOREIGN KEY (posicionamento_supar_id)
	REFERENCES posicionamento (id);
--rollback ALTER TABLE proposicao drop CONSTRAINT fk_supar_posicao


