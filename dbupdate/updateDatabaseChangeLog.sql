-- *********************************************************************
-- SQL to add all changesets to database history table
-- *********************************************************************
-- Change Log: /home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml
-- Ran at: 11/10/15 2:18 PM
-- Against: sislegis@jdbc:postgresql://localhost:5432/sislegis
-- Liquibase version: 3.4.1
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE public.databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM public.databasechangeloglock;

INSERT INTO public.databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE public.databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'fe80:0:0:0:9665:9cff:fe6e:a357%wlan0 (fe80:0:0:0:9665:9cff:fe6e:a357%wlan0)', LOCKGRANTED = '2015-11-10 14:18:03.503' WHERE ID = 1 AND LOCKED = FALSE;

-- Create Database Change Log Table
CREATE TABLE public.databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITHOUT TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255));

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-1', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 1, '7:afe897bea20bc08d1681087c94d0967d', 'createSequence', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-2', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 2, '7:5020f8c1a9dfd660abb7cd766f0de0cc', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-3', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 3, '7:9ac713a92ae1eeb3e8abb638001a9870', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-4', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 4, '7:57779f5a1bb783ffc48f7c91c026e147', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-5', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 5, '7:57acf2755feadeb02d7889da855f9ce3', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-6', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 6, '7:a73c6026218a1a63653ebf2672672edf', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-7', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 7, '7:47f9ce3d8727ee18dd0347d63e4f4dc2', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-8', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 8, '7:f3631076f03a095074a2c9038761cc62', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-9', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 9, '7:4efd07831ca10cf784d425290ca9f68a', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-10', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 10, '7:96028e01ba8348de18a8e4c7e3aed324', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-11', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 11, '7:8dbac7b5b6c24dd5181e40b1b94e9086', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-12', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 12, '7:34ec197de975244b25e8377638fadbcc', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-13', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 13, '7:a4391c64862bda401e8b94f954efddd8', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-14', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 14, '7:211cfb37ac385640bd953cd52e5b517b', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-15', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 15, '7:7ba30f8949931b70cee99724a7d17921', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-16', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 16, '7:06ac922c0669a008c65271fa8a283334', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-17', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 17, '7:1a70c5a43ed3ecf7e346b765df9e0f24', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-18', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 18, '7:abb6e5e0f2a24959aedf618f093e9130', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-19', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 19, '7:acd5ed1e3f8cde70c937e3ff77fe102c', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-20', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 20, '7:e7116fab6ecb447fb8769aae08a84fbf', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-21', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 21, '7:49ba454c50dde0996a9e017e2b4846af', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-22', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 22, '7:df435117ec0c1b5a5183542d86c25474', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-23', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 23, '7:d6379069f3004fe882e67771fa5ee888', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-24', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 24, '7:897f986f7a948bb229a34583b572968a', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-25', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 25, '7:e3030eea6f9b46091571ab2d9d245f55', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-26', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 26, '7:18d88267bafc48bd6e6dcb54533db04b', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-27', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 27, '7:5009417217698bb0b2f2036e2cd74994', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-28', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 28, '7:3854248ce762617e5e36f96bb3955197', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-29', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 29, '7:a4cb849118a6432525af4282f14b3c65', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-30', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 30, '7:0e0ee40d702fe3068131d5fbf61f8396', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-31', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 31, '7:36646b6678952342f4c1751be977c098', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-32', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 32, '7:24ec87cde1003ba64f0903aa8f443353', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-33', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 33, '7:9d8c807d621aecbb06f90f862707b6ea', 'createTable', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-34', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 34, '7:546a3b4ee8c752e9509cd6a003afe55e', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-35', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 35, '7:b42721dea008edf145db4955bb434784', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-36', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 36, '7:7705f053d51f6434c4b7ab3eb4f22a5c', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-37', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 37, '7:152e5d301c8a8e2266b1ee6a9e383b8e', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-38', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 38, '7:600c2bcc2ace7d99ec3a1ed20ca17aa7', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-39', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 39, '7:c4180e5a7f71a08fd8c4adb9e21375c0', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-40', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 40, '7:c868dcba8347ee301ae325548a964db9', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-41', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 41, '7:00ec48680b370f6453defc5995d2bde8', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-42', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 42, '7:d503b82f0f013089795897f716454abd', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-43', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 43, '7:ea5fec515920ad98b87a43123c4bd709', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-44', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 44, '7:5af676fcb01823e06efbaebcac935230', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-45', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 45, '7:eb6d808c713728cb95774ee2d7ae004a', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-46', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 46, '7:f113f90a7e32eee9c8c955d349d7fdfb', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-47', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 47, '7:0ee684a8ef651bf2a6bbec0dee5d30d0', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-48', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 48, '7:c8a27491442bb2dcf83208627053cc32', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-49', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 49, '7:a4a2a5c07725de6b339da60fe11be75a', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-50', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 50, '7:0182b978e11c8fb17bdf56d9e70f2035', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-51', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 51, '7:f59505410cf41a7efdc0b26597c52923', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-52', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 52, '7:53a27ac217f6c4f1be6e45ad76a9ff48', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-53', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 53, '7:2b1e40d406c6cf6576765340efe4f8e2', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-54', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 54, '7:49e682769c024d37da1540c278e7c4f1', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-55', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 55, '7:1ac75ebabbbfd40bdb725a0564235fb8', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-56', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 56, '7:fc8fc5843f8f6570235eb850c1fd9542', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-57', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 57, '7:99a64d746ead5a73381043ca3f9336ba', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-58', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 58, '7:f7fed41b485e3ccf42a788806021690d', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-59', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 59, '7:12559e3498685b888681e7965543cd1a', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-60', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 60, '7:88cdd9760a0dfd1a934af8819916ffd5', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-61', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 61, '7:d5fd80c7dcfa60f7e905fb53cae907d9', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-62', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 62, '7:f775bc4bfe793e2a97bf4e6ffaf3dca3', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-63', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 63, '7:531617fac7b270f21c769da99b66e0c3', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-64', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 64, '7:b48b21fa520a564aee0053e0a210f590', 'addPrimaryKey', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-65', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 65, '7:1871ac9492fd9ba20e8fc1aa7f6fc58e', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-66', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 66, '7:98c7f172076b3bd3124f561a7f714ceb', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-67', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 67, '7:4349dd611d2f6186fe44325f1d0eb6ed', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-68', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 68, '7:fa77d063e1ec6da3d034ca5fa3bd2568', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-69', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 69, '7:a8d1f647797cad8825b697b8541d54af', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-70', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 70, '7:cd97300ede97637e7432866060893c9a', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-71', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 71, '7:d7d4ba246907aef57a1b6bbfa183db46', 'addUniqueConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-72', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 72, '7:166bbc35eae75c3ebc2830a2c57e0251', 'createIndex', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-73', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 73, '7:e7f4d362fcb2afe81c9dca0be9e0cb0a', 'createIndex', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-74', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 74, '7:398b02239b5ba5b44bdab6daa10ce72a', 'createIndex', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-75', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 75, '7:9f9604a49b4d646e444eb04c31ecb4f4', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-76', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 76, '7:4841b37f2b353c03faf6a585c00552d5', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-77', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 77, '7:4bc5ab44b636ca22f9e0381c0903c6ff', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-78', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 78, '7:a0708a323bc721d2b0424a5728d94981', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-79', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 79, '7:74635b9660d09db1e9dcfb16e2395e68', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-80', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 80, '7:90013f28c3dce6bdc9274c1955d29c63', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-81', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 81, '7:5866e32605848d1fc16b04b74566521f', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-82', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 82, '7:8440866b331b37b16a24846035d96159', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-83', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 83, '7:9486f58d2fc48f8bcbef60584ce53ea6', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-84', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 84, '7:7390a0cbd50f0abf5b5bafd67cb8fe38', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-85', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 85, '7:2671e94d41daababe71f68b36b1b34eb', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-86', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 86, '7:50ff48d4160e84220835af238c1a2ce9', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-87', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 87, '7:ab949590494fd698013d3a762f997fad', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-88', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 88, '7:c841668c447e162bc9414faf749730d1', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-89', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 89, '7:c4de0c48106042ea8a06bbeb55e5b8c3', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-90', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 90, '7:c8cee206288f28d9895ea3c3f345be3b', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-91', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 91, '7:85f390142a3e11977514fd923988583a', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-92', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 92, '7:3c31bd37c4cc83c733c640c57015c479', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-93', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 93, '7:5863de1fba4c028c6955e5f9645b37e8', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-94', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 94, '7:ac46450702abc45b4edbc32517a1bbc5', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-95', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 95, '7:e97185c899ac45932351e86f68f83ae1', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-96', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 96, '7:430807bb035404b42c394ea7de68b93b', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-97', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 97, '7:a0dd7b019ec5b5c59cd1aa19a0442f51', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-98', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 98, '7:aa563849bb18a3019254a596f5c849a9', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-99', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 99, '7:30a9e13f13d79d162a93a7bb2c76b7e2', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-100', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 100, '7:5d3f890c468101e1885d4caed71b59e2', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-101', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 101, '7:2d379a870500740035722dc84cfd6739', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-102', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 102, '7:baee66ffdba5efbb4fdcc90053880866', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-103', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 103, '7:399756da822d33e1ed0bdc2693a39219', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-104', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 104, '7:00a09e690bf2057e71f82e26a32d87ea', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-105', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 105, '7:a64a10e5681ec447d5c4cd7fcc9a264c', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-106', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 106, '7:31367bf769b171e2a3a3c402765585ac', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-107', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 107, '7:49de778c8dabb8b840c428aa0df7be84', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-108', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 108, '7:8560479368eb8d0c507703e77af18f22', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-109', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 109, '7:b9d55add34647bfd5da3663ff0577351', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-110', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 110, '7:4c410a5e2be64ab7c439b50a8cec1d21', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-111', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 111, '7:dda885373bcb9543f69135a0de525daa', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-112', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 112, '7:4e2e17a8723b65837521b55ba1f94815', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-113', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 113, '7:60d03fb173963d936546caa5554960f4', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-114', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 114, '7:750a0f7d08b2e5f39d11197d167a0ef3', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-115', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 115, '7:caa35981d6e32f4ab62ecf67bfdcc43e', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-116', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 116, '7:6cea8826a35088255e7fe3db8c1586c3', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('1447106279783-117', 'sislegis (generated)', '/home/sislegis/projetos/app/dbupdate/sislegisdb.install.changelog.xml', NOW(), 117, '7:c976f0244ad08e61cf9bd4edc4831a02', 'addForeignKeyConstraint', '', 'EXECUTED', 'legacy', NULL, '3.4.1');

-- Release Database Lock
UPDATE public.databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

