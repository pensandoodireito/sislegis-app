--liquibase formatted sql
ALTER TABLE public.posicionamento ADD preliminar BOOLEAN DEFAULT FALSE NOT NULL;
