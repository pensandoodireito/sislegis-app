--liquibase formatted sql
--changeset release151002:1447078335578-116  
alter table proposicao ADD ultima_comissao varchar(200);
update proposicao set ultima_comissao=reuniaoproposicao.siglacomissao from reuniaoproposicao where reuniaoproposicao.proposicao_id=id;
update proposicao set ultima_comissao=trimmed from (select id,trim(both ' ' from substr(ultima_comissao,0,endsigla)) as trimmed from (select id,ultima_comissao,strpos(ultima_comissao,'-') as endsigla from proposicao where ultima_comissao is not null) indexeds where indexeds.endsigla>0) fixed where fixed.id=proposicao.id;
--rollback alter table proposicao drop ultima_comissao

--changeset release151002:1447078335578-117
ALTER TABLE encaminhamento RENAME TO tipo_encaminhamento;
--rollback alter table ALTER TABLE tipo_encaminhamento RENAMERENAME TO encaminhamento

--changeset release151002:1447078335578-118
ALTER TABLE encaminhamentoproposicao RENAME COLUMN encaminhamento_id TO tipo_encaminhamento_id;
--rollback alter table ALTER TABLE encaminhamentoproposicao RENAME COLUMN tipo_encaminhamento_id TO encaminhamento_id
