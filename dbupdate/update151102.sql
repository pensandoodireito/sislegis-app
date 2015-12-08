--liquibase formatted sql

--changeset release151102:151102-1
create TABLE etapa_roadmap_comissao (
    id bigint not null,
    proposicao_id bigint not null,
    comissao varchar(50) not null,
    ordem integer not null,
    PRIMARY KEY (id),
    FOREIGN KEY (proposicao_id) REFERENCES proposicao (id)
);
--rollback drop table etapa_roadmap_comissao
