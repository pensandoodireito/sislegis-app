
--changeset coutinho:161118-1
alter table proposicao ADD foiAtribuida BIGINT default null;
UPDATE proposicao B SET
     foiAtribuida = (SELECT foiEncaminhada FROM proposicao WHERE ID = B.ID); 
--rollback alter table proposicao drop foiAtribuida


--changeset coutinho:161118-2
alter table proposicao_notatecnica ADD criacao BIGINT default null;
UPDATE proposicao_notatecnica B SET
     criacao = (SELECT (extract(epoch from datacriacao::timestamp with time zone)*1000) FROM proposicao_notatecnica WHERE ID = B.ID)
--rollback alter table proposicao_notatecnica drop criacao

--changeset coutinho:161118-3
alter table proposicao_emenda ADD criacao BIGINT default null;
UPDATE proposicao_emenda B SET
     criacao = (SELECT (extract(epoch from datacriacao::timestamp with time zone)*1000) FROM proposicao_emenda WHERE ID = B.ID)
--rollback alter table proposicao_emenda drop criacao

     
--changeset coutinho:161118-4
alter table proposicao_briefing ADD criacao BIGINT default null;
UPDATE proposicao_briefing B SET
     criacao = (SELECT (extract(epoch from datacriacao::timestamp with time zone)*1000) FROM proposicao_briefing WHERE ID = B.ID)
--rollback alter table proposicao_briefing drop criacao



     