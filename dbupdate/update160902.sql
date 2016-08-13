

--liquibase formatted sql

--changeset delgado:160902-1
CREATE TABLE usuario_papel
(  
  papel VARCHAR(50) NOT NULL,
  usuario_id bigint not null,
  CONSTRAINT usuario_id_fk FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
--rollback drop table usuario_papel
CREATE UNIQUE INDEX usuario_papel_uindex ON usuario_papel (usuario_id, papel);
--rollback DROP INDEX public.usuario_papel_uindex CASCADE
