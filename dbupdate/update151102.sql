--liquibase formatted sql

--changeset coutinho:151102-1
alter table proposicao ADD posicionamento_atual_id INT8;
--rollback alter table proposicao drop posicionamento_atual

--changeset coutinho:151102-2
update proposicao p  set posicionamento_atual_id=f.id from  (select pp.proposicao_id, pp.id from posicionamento_proposicao pp, (select distinct proposicao_id, max(datacriacao) as maxdatacriacao from posicionamento_proposicao group by proposicao_id) pm where pp.proposicao_id=pm.proposicao_id and pp.datacriacao=pm.maxdatacriacao) f where p.id=f.proposicao_id
--rollback alter table proposicao drop posicionamento_atual

