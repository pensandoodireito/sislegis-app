--changeset coutinho:161125-1
alter table proposicao ADD resultado_congresso varchar(50) default 'EM_TRAMITACAO';
--rollback alter table proposicao drop resultado_congresso

--changeset coutinho:161031-5
alter table proposicao ADD teveResultado BIGINT default null;
--rollback alter table proposicao drop teveResultado