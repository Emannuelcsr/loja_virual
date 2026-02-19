CREATE TABLE IF NOT EXISTS public.tabela_acesso_end_point
(
    qtd_acesso_end_point integer,
    nome_end_point character varying COLLATE pg_catalog."default"
);

INSERT INTO public.tabela_acesso_end_point(
	qtd_acesso_end_point, nome_end_point)
	VALUES (0, 'END-POINT-NOME-PESSOA-FISICA');
	
	alter table tabela_acesso_end_point add constraint nome_end_point_unique UNIQUE (nome_end_point);