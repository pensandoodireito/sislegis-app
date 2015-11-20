insert into Posicionamento (nome, id) values ('Acompanhar Relator', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Acompanhar Substitutivo', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Contrário', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Contrário com Emendas', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Elaborar VTS', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Favorável', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Favorável com Emendas', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Indiferente', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Nada a opor', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Não é tema do MJ', nextval ('hibernate_sequence'));
insert into Posicionamento (nome, id) values ('Seguir pela Rejeição', nextval ('hibernate_sequence'));

insert into tipo_encaminhamento (nome, id) values ('Agendar Reunião', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Apenas Monitorar', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Cobrar Posicionamento', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Despachar Nota Técnica com o Secretário', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Elaborar Nota Técnica', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Elaborar VTS', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Enviar E-mail', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Fazer contato telefônico', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Incluir na Pauta Prioritária', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Pedir Retirada de Pauta', nextval ('hibernate_sequence'));
insert into tipo_encaminhamento (nome, id) values ('Retirar da Pauta do MJ', nextval ('hibernate_sequence'));

INSERT INTO Usuario (id, nome, email) VALUES(nextval ('hibernate_sequence'), 'Mat Oliveira', 'mn@foo.com');
INSERT INTO Usuario (id, nome, email) VALUES(nextval ('hibernate_sequence'), 'Guilherme Almeida', 'galm@foo.com');
INSERT INTO Usuario (id, nome, email) VALUES(nextval ('hibernate_sequence'), 'Sabrina', 'sa.ma@foo.com');
INSERT INTO Usuario (id, nome, email) VALUES(nextval ('hibernate_sequence'), 'Anna Claudia', 'ac@foo.com');
