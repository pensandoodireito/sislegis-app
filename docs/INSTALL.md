Documento de instalação Sislegis-app
===================

Este documento descreve o processo para instalação do Sislegis-app.

----------


Pré-requisitos
-------------

Os seguintes pré-requisitos são necessários para deploy do Sislegis-app

 - Java 8+
 - WildFly 8.2.0.Final
 - KeyCloak 1.2.0 +
 - Postgresql 9.3+
 - Linux Debian / Ubuntu 14+

Para fazer o build dos Sislegis-app além dos itens acima são necessários:

 - Java 8 JDK
 - Maven 3+
 - Liquibase 3.4.1+

Para desenvolvimento sugere-se também:

 - Eclipse Luna + 
 
Compilação
-------------

Para construir o WAR execute 

    mvn package
Para instalar diretamente no WildFly local utilize:

    mvn install

Para também executar o upgrade de banco utilize:

    mvn install -P upgrade


Criação do Banco de Dados
-------------------------

Para criar o banco de dados inicial crie-o no postgres:

    sudo -u postgres psql -c "drop database sislegis;"
    sudo -u postgres psql -c "create database sislegis;"
   
   Então utilize o maven com o profile upgrade mas sete o seguinte arquivo como arquivo de mudanças (changelogfile):

    dbupdate/sislegisdb.install.changelog.xml
 E em seguida o arquivo:

    dbupdate/sislegisdb.postinstall.changelog.xml
*  Isto se deve ao fato de existir uma base original de produção no nível setado pelo script 1. Por isso a separação.

Configuração Keycloak
-------------------------
Para configurar o Keycloak inicie o WildFly e acesse a interface administrativa do keycloak:

    http://localhost:8080/auth
  Senha usuário padrão é admin/admin
      Clique em adicionar Realm (botão no topo direito). Selecione o arquivo do ambiente sislegis `configuracoes/keycloak/keycloak.json` 
  No ambiente de desenveolvimento utilize 
      `configuracoes/keycloak/keycloak.json.desenvolvimento` 
