
--changeset coutinho:161118-1
alter table proposicao ADD foiAtribuida BIGINT default null;
UPDATE proposicao B SET  foiEncaminhada = (SELECT (extract(epoch from created::timestamp with time zone)*1000) FROM proposicao WHERE ID = B.ID) where foiEncaminhada is null;
UPDATE proposicao B SET  foiAtribuida = (SELECT foiEncaminhada FROM proposicao WHERE ID = B.ID) where B.responsavel_id is not null;
UPDATE proposicao B SET  foiAnalisada = (SELECT (extract(epoch from updated::timestamp with time zone)*1000) FROM proposicao WHERE ID = B.ID) where foiRevisada is null and estado<>'EMANALISE' and estado<>'FORADECOMPETENCIA' and estado<>'INCLUIDO';
UPDATE proposicao B SET  foiRevisada = (SELECT (extract(epoch from updated::timestamp with time zone)*1000) FROM proposicao WHERE ID = B.ID) where foiRevisada is null and estado<>'EMANALISE' and estado<>'FORADECOMPETENCIA' and estado<>'INCLUIDO' and estado<>'ANALISADA';
UPDATE proposicao B SET  foiDespachada = (SELECT (extract(epoch from updated::timestamp with time zone)*1000) FROM proposicao WHERE ID = B.ID) where foiDespachada is null and estado='DESPACHADA';
UPDATE proposicao B SET  foiAtribuida = (SELECT foiAnalisada FROM proposicao WHERE ID = B.ID) where foiAtribuida is null and B.responsavel_id is not null;
--rollback alter table proposicao drop foiAtribuida

--changeset coutinho:161118-2
alter table proposicao_notatecnica ADD criacao BIGINT default null;
UPDATE proposicao_notatecnica B SET criacao = (SELECT (extract(epoch from datacriacao::timestamp with time zone)*1000) FROM proposicao_notatecnica WHERE ID = B.ID);
--rollback alter table proposicao_notatecnica drop criacao

--changeset coutinho:161118-2.1
alter table proposicao_notatecnica drop dataCriacao;
--rollback 


--changeset coutinho:161118-3
alter table proposicao_emenda ADD criacao BIGINT default null;
UPDATE proposicao_emenda B SET  criacao = (SELECT (extract(epoch from datacriacao::timestamp with time zone)*1000) FROM proposicao_emenda WHERE ID = B.ID);
--rollback alter table proposicao_emenda drop criacao

--changeset coutinho:161118-3.1
alter table proposicao_emenda drop dataCriacao;
--rollback

     
--changeset coutinho:161118-4
alter table proposicao_briefing ADD criacao BIGINT default null;
UPDATE proposicao_briefing B SET  criacao = (SELECT (extract(epoch from datacriacao::timestamp with time zone)*1000) FROM proposicao_briefing WHERE ID = B.ID);
--rollback alter table proposicao_briefing drop criacao

--changeset coutinho:161118-4.1
alter table proposicao_briefing drop dataCriacao;
--rollback


