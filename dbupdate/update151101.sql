--liquibase formatted sql

--changeset release151101:151101-1  
create table notificacao( 
		id INT8 NOT NULL PRIMARY KEY,
		criada_em TIMESTAMP,
		vista_em TIMESTAMP,
		usuario_id INT8,
		categoria VARCHAR(30),
		identificacao_entidade VARCHAR(10),
		descricao VARCHAR(128)
	);

--changeset release151101:151101-4
CREATE INDEX idx_notificacao_user ON notificacao (usuario_id);
--rollback drop index notificacao_pkey 

--changeset release151101:151101-5
ALTER TABLE notificacao ADD CONSTRAINT fk_not_user FOREIGN KEY (usuario_id)
	REFERENCES usuario (id);
--rollback ALTER TABLE notificacao drop CONSTRAINT fk_not_user

--changeset release151101:151101-6
insert into notificacao (id,categoria,usuario_id,criada_em,vista_em,descricao,identificacao_entidade) select nextval ('hibernate_sequence'),'TAREFAS',t.usuario_id,now(),NULL,ept.nome,t.id from tarefa t,encaminhamentoproposicao ep,tipo_encaminhamento ept where t.encaminhamentoproposicao_id=ep.id and ept.id=ep.tipo_encaminhamento_id
--rollback delete from notificacao

--changeset release151101:151101-7
alter table tarefa drop isvisualizada
