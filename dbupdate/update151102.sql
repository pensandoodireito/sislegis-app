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
