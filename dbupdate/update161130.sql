--changeset coutinho:161130-1
update proposicao set foianalisada=foiatribuida  where foianalisada is not null and foianalisada<foiatribuida;
--rollback 
