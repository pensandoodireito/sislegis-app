--changeset coutinho:161031-1
alter table proposicao ADD tramitacao varchar(5000) default null;
--rollback alter table proposicao drop tramitacao

--changeset coutinho:161031-2
alter table proposicao ADD foiEncaminhada BIGINT default null;
--rollback alter table proposicao drop foiEncaminhada

--changeset coutinho:161031-3
alter table proposicao ADD foiAnalisada BIGINT default null;
--rollback alter table proposicao drop foiAnalisada

--changeset coutinho:161031-4
alter table proposicao ADD foiRevisada BIGINT default null;
--rollback alter table proposicao drop foiRevisada

--changeset coutinho:161031-5
alter table proposicao ADD foiDespachada BIGINT default null;
--rollback alter table proposicao drop foiDespachada

--changeset coutinho:161031-6
alter table proposicao ADD comAtencaoEspecial BIGINT default null;
--rollback alter table proposicao drop foiMarcadaRevisaoMinistro

