--liquibase formatted sql

--changeset release151102:151102-1
CREATE TABLE etapa_roadmap_comissao
(
    id BIGINT PRIMARY KEY NOT NULL,
    proposicao_id BIGINT NOT NULL,
    comissao_id BIGINT NOT NULL,
    ordem INT NOT NULL
);
--rollback drop table etapa_roadmap_comissao

--changeset release151102:151102-2
ALTER TABLE etapa_roadmap_comissao
ADD CONSTRAINT etapa_roadmap_comissao_comissao_id_fk
FOREIGN KEY (comissao_id) REFERENCES comissao (id);
--rollback ALTER TABLE etapa_roadmap_comissao DROP CONSTRAINT etapa_roadmap_comissao_comissao_id_fk

--changeset release151102:151102-3
ALTER TABLE etapa_roadmap_comissao
ADD CONSTRAINT etapa_roadmap_comissao_proposicao_id_fk
FOREIGN KEY (proposicao_id) REFERENCES proposicao (id);
--rollback ALTER TABLE etapa_roadmap_comissao DROP CONSTRAINT etapa_roadmap_comissao_proposicao_id_fk