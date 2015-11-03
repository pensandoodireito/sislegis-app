alter table proposicao ADD ultima_comissao varchar(200) 

update proposicao set ultima_comissao=rp.siglacomissao from reuniaoproposicao rp where rp.proposicao_id=id

update proposicao set ultima_comissao=trimmed from (select id,trim(both ' ' from substr(ultima_comissao,0,endsigla)) as trimmed from (select id,ultima_comissao,strpos(ultima_comissao,'-') as endsigla from proposicao where ultima_comissao is not null) indexeds where indexeds.endsigla>0) fixed where fixed.id=proposicao.id;
