--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.17
-- Dumped by pg_dump version 9.5.17

-- Started on 2026-02-02 15:14:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2345 (class 1262 OID 16393)
-- Name: loja_virtual_teste; Type: DATABASE; Schema: -; Owner: postgres
--

--CREATE DATABASE loja_virtual_teste WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';


ALTER DATABASE loja_virtual_teste OWNER TO postgres;

--\connect loja_virtual_teste

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2348 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 221 (class 1255 OID 34634)
-- Name: valida_pessoa_em_endereco(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.valida_pessoa_em_endereco() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  existe integer;
BEGIN
  SELECT count(1) INTO existe
  FROM pessoa_fisica
  WHERE id = NEW.pessoa_id;

  IF existe <= 0 THEN
    SELECT count(1) INTO existe
    FROM pessoa_juridica
    WHERE id = NEW.pessoa_id;

    IF existe <= 0 THEN
      RAISE EXCEPTION 'Nao foi encontrado o id da pessoa (PF ou PJ) para associar ao endereco. pessoa_id=%', NEW.pessoa_id;
    END IF;
  END IF;

  RETURN NEW;
END;
$$;


ALTER FUNCTION public.valida_pessoa_em_endereco() OWNER TO postgres;

--
-- TOC entry 234 (class 1255 OID 34639)
-- Name: valida_pessoa_em_endereco2(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.valida_pessoa_em_endereco2() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  existe integer;
BEGIN
  SELECT count(1) INTO existe
  FROM pessoa_fisica
  WHERE id = NEW.pessoa_forn_id;

  IF existe <= 0 THEN
    SELECT count(1) INTO existe
    FROM pessoa_juridica
    WHERE id = NEW.pessoa_forn_id;

    IF existe <= 0 THEN
      RAISE EXCEPTION 'Nao foi encontrado o id da pessoa (PF ou PJ) para associar ao endereco. pessoa_id=%', NEW.pessoa_id;
    END IF;
  END IF;

  RETURN NEW;
END;
$$;


ALTER FUNCTION public.valida_pessoa_em_endereco2() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 200 (class 1259 OID 34408)
-- Name: acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acesso (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.acesso OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 34413)
-- Name: avaliacao_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.avaliacao_produto (
    nota integer NOT NULL,
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    produto_id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.avaliacao_produto OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 34418)
-- Name: categoria_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria_produto (
    id bigint NOT NULL,
    nome_desc character varying(255) NOT NULL
);


ALTER TABLE public.categoria_produto OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 34423)
-- Name: conta_pagar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conta_pagar (
    date_pagamento date,
    date_vencimento date NOT NULL,
    valor_desconto numeric(38,2),
    valor_total numeric(38,2) NOT NULL,
    id bigint NOT NULL,
    pessoa_forn_id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    status_conta_pagar character varying(255) NOT NULL,
    CONSTRAINT conta_pagar_status_conta_pagar_check CHECK (((status_conta_pagar)::text = ANY ((ARRAY['COBRANCA'::character varying, 'VENCIDA'::character varying, 'ABERTA'::character varying, 'QUITADA'::character varying, 'NEGOCIADA'::character varying])::text[])))
);


ALTER TABLE public.conta_pagar OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 34432)
-- Name: conta_receber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conta_receber (
    date_pagamento date,
    date_vencimento date NOT NULL,
    valor_desconto numeric(38,2),
    valor_total numeric(38,2) NOT NULL,
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    status_conta_receber character varying(255) NOT NULL,
    CONSTRAINT conta_receber_status_conta_receber_check CHECK (((status_conta_receber)::text = ANY ((ARRAY['COBRANCA'::character varying, 'VENCIDA'::character varying, 'ABERTA'::character varying, 'QUITADA'::character varying])::text[])))
);


ALTER TABLE public.conta_receber OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 34441)
-- Name: cupom_desconto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cupom_desconto (
    data_validade_cupom date NOT NULL,
    valor_porc_desc numeric(38,2),
    valor_real_desc numeric(38,2),
    id bigint NOT NULL,
    cod_descricao character varying(255) NOT NULL
);


ALTER TABLE public.cupom_desconto OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 34446)
-- Name: endereco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.endereco (
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    bairro character varying(255) NOT NULL,
    cep character varying(255) NOT NULL,
    cidade character varying(255) NOT NULL,
    complemtento character varying(255),
    numero character varying(255) NOT NULL,
    rua_logra character varying(255) NOT NULL,
    tipo_endereco character varying(255) NOT NULL,
    uf character varying(255) NOT NULL,
    CONSTRAINT endereco_tipo_endereco_check CHECK (((tipo_endereco)::text = ANY ((ARRAY['COBRANCA'::character varying, 'ENTREGA'::character varying])::text[])))
);


ALTER TABLE public.endereco OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 34455)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.forma_pagamento (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.forma_pagamento OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 34460)
-- Name: imagem_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.imagem_produto (
    id bigint NOT NULL,
    produto_id bigint NOT NULL,
    imagem_miniatura text NOT NULL,
    imagem_original text NOT NULL
);


ALTER TABLE public.imagem_produto OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 34468)
-- Name: item_venda_loja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_venda_loja (
    quantidade double precision NOT NULL,
    id bigint NOT NULL,
    produto_id bigint NOT NULL,
    venda_compra_loja_virtual_teste_id bigint NOT NULL
);


ALTER TABLE public.item_venda_loja OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 34473)
-- Name: marca_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.marca_produto (
    id bigint NOT NULL,
    nome_desc character varying(255) NOT NULL
);


ALTER TABLE public.marca_produto OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 34478)
-- Name: nota_fiscal_compra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nota_fiscal_compra (
    data_compra date NOT NULL,
    valor_desconto numeric(38,2),
    valor_icms numeric(38,2) NOT NULL,
    valor_total numeric(38,2) NOT NULL,
    conta_pagar_id bigint NOT NULL,
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    descricao_obs character varying(255),
    numero_nota character varying(255) NOT NULL,
    serie_nota character varying(255) NOT NULL
);


ALTER TABLE public.nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 34486)
-- Name: nota_fiscal_venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nota_fiscal_venda (
    id bigint NOT NULL,
    venda_compra_loja_virtual_teste_id bigint NOT NULL,
    numero_nota character varying(255) NOT NULL,
    pdf text NOT NULL,
    serie_nota character varying(255) NOT NULL,
    tipo_nota character varying(255) NOT NULL,
    xml text NOT NULL
);


ALTER TABLE public.nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 34496)
-- Name: nota_item_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nota_item_produto (
    quantidade double precision NOT NULL,
    id bigint NOT NULL,
    nota_fiscal_compra_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE public.nota_item_produto OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 34501)
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_fisica (
    data_nascimento date,
    id bigint NOT NULL,
    cpf character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL
);


ALTER TABLE public.pessoa_fisica OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 34509)
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_juridica (
    id bigint NOT NULL,
    categoria character varying(255),
    cnpj character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    insc_estadual character varying(255) NOT NULL,
    insc_municipal character varying(255),
    nome character varying(255) NOT NULL,
    nome_fantasia character varying(255) NOT NULL,
    razao_social character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL
);


ALTER TABLE public.pessoa_juridica OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 34517)
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produto (
    alerta_quantidade_estoque boolean,
    altura double precision NOT NULL,
    ativo boolean NOT NULL,
    largura double precision NOT NULL,
    peso double precision NOT NULL,
    profundidade double precision NOT NULL,
    quantidade_alerta_estoque integer,
    quantidade_click integer,
    quantidade_estoque integer NOT NULL,
    valor_venda numeric(38,2) NOT NULL,
    id bigint NOT NULL,
    descricao text NOT NULL,
    link_youtube character varying(255),
    nome character varying(255) NOT NULL,
    tipo_unidade character varying(255) NOT NULL
);


ALTER TABLE public.produto OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 34370)
-- Name: seq_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_acesso OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 34372)
-- Name: seq_avaliacao_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_avaliacao_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_avaliacao_produto OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 34374)
-- Name: seq_categoria_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_categoria_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_categoria_produto OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 34376)
-- Name: seq_conta_pagar; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_conta_pagar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_conta_pagar OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 34378)
-- Name: seq_conta_receber; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_conta_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_conta_receber OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 34380)
-- Name: seq_cupom_desconto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_cupom_desconto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_cupom_desconto OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 34382)
-- Name: seq_endereco; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_endereco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_endereco OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 34384)
-- Name: seq_forma_pagamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_forma_pagamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_forma_pagamento OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 34386)
-- Name: seq_imagem_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_imagem_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_imagem_produto OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 34388)
-- Name: seq_item_venda_loja; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_item_venda_loja
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_item_venda_loja OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 34390)
-- Name: seq_marca_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_marca_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_marca_produto OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 34392)
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_nota_fiscal_compra
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 34394)
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_nota_fiscal_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 34396)
-- Name: seq_nota_item_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_nota_item_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_nota_item_produto OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 34398)
-- Name: seq_pessoa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_pessoa OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 34400)
-- Name: seq_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_produto OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 34402)
-- Name: seq_status_rastreio; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_status_rastreio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_status_rastreio OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 34404)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_usuario OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 34406)
-- Name: seq_venda_compra_loja_virtual_teste; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_venda_compra_loja_virtual_teste
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_venda_compra_loja_virtual_teste OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 34525)
-- Name: status_rastreio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.status_rastreio (
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    venda_compra_loja_virtual_teste_id bigint NOT NULL,
    centro_distribuicao character varying(255),
    cidade character varying(255),
    estado character varying(255),
    status character varying(255)
);


ALTER TABLE public.status_rastreio OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 34533)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    data_atual_senha date NOT NULL,
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    login character varying(255) NOT NULL,
    senha character varying(255) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 34541)
-- Name: usuarios_acessos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios_acessos (
    acesso_id bigint NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.usuarios_acessos OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 34546)
-- Name: venda_compra_loja_virtual_teste; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venda_compra_loja_virtual_teste (
    data_entrega date NOT NULL,
    dias_entrega integer NOT NULL,
    valor_desconto numeric(38,2),
    valor_frete numeric(38,2) NOT NULL,
    valor_total numeric(38,2) NOT NULL,
    cupom_desconto_id bigint,
    endereco_cobranca_id bigint NOT NULL,
    endereco_entrega_id bigint NOT NULL,
    form_pagamento_id bigint NOT NULL,
    id bigint NOT NULL,
    nota_fiscal_venda_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE public.venda_compra_loja_virtual_teste OWNER TO postgres;

--
-- TOC entry 2319 (class 0 OID 34408)
-- Dependencies: 200
-- Data for Name: acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2320 (class 0 OID 34413)
-- Dependencies: 201
-- Data for Name: avaliacao_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.avaliacao_produto (nota, id, pessoa_id, produto_id, descricao) VALUES (6, 1, 1, 1, 'AFASFSA');


--
-- TOC entry 2321 (class 0 OID 34418)
-- Dependencies: 202
-- Data for Name: categoria_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2322 (class 0 OID 34423)
-- Dependencies: 203
-- Data for Name: conta_pagar; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2323 (class 0 OID 34432)
-- Dependencies: 204
-- Data for Name: conta_receber; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2324 (class 0 OID 34441)
-- Dependencies: 205
-- Data for Name: cupom_desconto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2325 (class 0 OID 34446)
-- Dependencies: 206
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2326 (class 0 OID 34455)
-- Dependencies: 207
-- Data for Name: forma_pagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2327 (class 0 OID 34460)
-- Dependencies: 208
-- Data for Name: imagem_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2328 (class 0 OID 34468)
-- Dependencies: 209
-- Data for Name: item_venda_loja; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2329 (class 0 OID 34473)
-- Dependencies: 210
-- Data for Name: marca_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2330 (class 0 OID 34478)
-- Dependencies: 211
-- Data for Name: nota_fiscal_compra; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2331 (class 0 OID 34486)
-- Dependencies: 212
-- Data for Name: nota_fiscal_venda; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2332 (class 0 OID 34496)
-- Dependencies: 213
-- Data for Name: nota_item_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2333 (class 0 OID 34501)
-- Dependencies: 214
-- Data for Name: pessoa_fisica; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.pessoa_fisica (data_nascimento, id, cpf, email, nome, telefone) VALUES (NULL, 1, 'FSAASF', 'FASFSA', 'FAFSA', 'AFSSFA');


--
-- TOC entry 2334 (class 0 OID 34509)
-- Dependencies: 215
-- Data for Name: pessoa_juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2335 (class 0 OID 34517)
-- Dependencies: 216
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.produto (alerta_quantidade_estoque, altura, ativo, largura, peso, profundidade, quantidade_alerta_estoque, quantidade_click, quantidade_estoque, valor_venda, id, descricao, link_youtube, nome, tipo_unidade) VALUES (true, 10, true, 10, 10, 10, 1, 1, 1, 1.00, 1, 'fassa', 'as', 'afs', 'fas');


--
-- TOC entry 2349 (class 0 OID 0)
-- Dependencies: 181
-- Name: seq_acesso; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_acesso', 1, false);


--
-- TOC entry 2350 (class 0 OID 0)
-- Dependencies: 182
-- Name: seq_avaliacao_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_avaliacao_produto', 1, false);


--
-- TOC entry 2351 (class 0 OID 0)
-- Dependencies: 183
-- Name: seq_categoria_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_categoria_produto', 1, false);


--
-- TOC entry 2352 (class 0 OID 0)
-- Dependencies: 184
-- Name: seq_conta_pagar; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_conta_pagar', 1, false);


--
-- TOC entry 2353 (class 0 OID 0)
-- Dependencies: 185
-- Name: seq_conta_receber; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_conta_receber', 1, false);


--
-- TOC entry 2354 (class 0 OID 0)
-- Dependencies: 186
-- Name: seq_cupom_desconto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_cupom_desconto', 1, false);


--
-- TOC entry 2355 (class 0 OID 0)
-- Dependencies: 187
-- Name: seq_endereco; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_endereco', 1, false);


--
-- TOC entry 2356 (class 0 OID 0)
-- Dependencies: 188
-- Name: seq_forma_pagamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_forma_pagamento', 1, false);


--
-- TOC entry 2357 (class 0 OID 0)
-- Dependencies: 189
-- Name: seq_imagem_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_imagem_produto', 1, false);


--
-- TOC entry 2358 (class 0 OID 0)
-- Dependencies: 190
-- Name: seq_item_venda_loja; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_item_venda_loja', 1, false);


--
-- TOC entry 2359 (class 0 OID 0)
-- Dependencies: 191
-- Name: seq_marca_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_marca_produto', 1, false);


--
-- TOC entry 2360 (class 0 OID 0)
-- Dependencies: 192
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_nota_fiscal_compra', 1, false);


--
-- TOC entry 2361 (class 0 OID 0)
-- Dependencies: 193
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_nota_fiscal_venda', 1, false);


--
-- TOC entry 2362 (class 0 OID 0)
-- Dependencies: 194
-- Name: seq_nota_item_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_nota_item_produto', 1, false);


--
-- TOC entry 2363 (class 0 OID 0)
-- Dependencies: 195
-- Name: seq_pessoa; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_pessoa', 1, false);


--
-- TOC entry 2364 (class 0 OID 0)
-- Dependencies: 196
-- Name: seq_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_produto', 1, false);


--
-- TOC entry 2365 (class 0 OID 0)
-- Dependencies: 197
-- Name: seq_status_rastreio; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_status_rastreio', 1, false);


--
-- TOC entry 2366 (class 0 OID 0)
-- Dependencies: 198
-- Name: seq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_usuario', 1, false);


--
-- TOC entry 2367 (class 0 OID 0)
-- Dependencies: 199
-- Name: seq_venda_compra_loja_virtual_teste; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_venda_compra_loja_virtual_teste', 1, false);


--
-- TOC entry 2336 (class 0 OID 34525)
-- Dependencies: 217
-- Data for Name: status_rastreio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2337 (class 0 OID 34533)
-- Dependencies: 218
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2338 (class 0 OID 34541)
-- Dependencies: 219
-- Data for Name: usuarios_acessos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2339 (class 0 OID 34546)
-- Dependencies: 220
-- Data for Name: venda_compra_loja_virtual_teste; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2114 (class 2606 OID 34412)
-- Name: acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acesso
    ADD CONSTRAINT acesso_pkey PRIMARY KEY (id);


--
-- TOC entry 2116 (class 2606 OID 34417)
-- Name: avaliacao_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.avaliacao_produto
    ADD CONSTRAINT avaliacao_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2118 (class 2606 OID 34422)
-- Name: categoria_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria_produto
    ADD CONSTRAINT categoria_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2120 (class 2606 OID 34431)
-- Name: conta_pagar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta_pagar
    ADD CONSTRAINT conta_pagar_pkey PRIMARY KEY (id);


--
-- TOC entry 2122 (class 2606 OID 34440)
-- Name: conta_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta_receber
    ADD CONSTRAINT conta_receber_pkey PRIMARY KEY (id);


--
-- TOC entry 2124 (class 2606 OID 34445)
-- Name: cupom_desconto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cupom_desconto
    ADD CONSTRAINT cupom_desconto_pkey PRIMARY KEY (id);


--
-- TOC entry 2126 (class 2606 OID 34454)
-- Name: endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 2128 (class 2606 OID 34459)
-- Name: forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2130 (class 2606 OID 34467)
-- Name: imagem_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.imagem_produto
    ADD CONSTRAINT imagem_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2132 (class 2606 OID 34472)
-- Name: item_venda_loja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_venda_loja
    ADD CONSTRAINT item_venda_loja_pkey PRIMARY KEY (id);


--
-- TOC entry 2134 (class 2606 OID 34477)
-- Name: marca_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marca_produto
    ADD CONSTRAINT marca_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2136 (class 2606 OID 34485)
-- Name: nota_fiscal_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_fiscal_compra
    ADD CONSTRAINT nota_fiscal_compra_pkey PRIMARY KEY (id);


--
-- TOC entry 2138 (class 2606 OID 34493)
-- Name: nota_fiscal_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_fiscal_venda
    ADD CONSTRAINT nota_fiscal_venda_pkey PRIMARY KEY (id);


--
-- TOC entry 2140 (class 2606 OID 34495)
-- Name: nota_fiscal_venda_venda_compra_loja_virtual_teste_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_fiscal_venda
    ADD CONSTRAINT nota_fiscal_venda_venda_compra_loja_virtual_teste_id_key UNIQUE (venda_compra_loja_virtual_teste_id);


--
-- TOC entry 2142 (class 2606 OID 34500)
-- Name: nota_item_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_item_produto
    ADD CONSTRAINT nota_item_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2144 (class 2606 OID 34508)
-- Name: pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id);


--
-- TOC entry 2146 (class 2606 OID 34516)
-- Name: pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id);


--
-- TOC entry 2148 (class 2606 OID 34524)
-- Name: produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2150 (class 2606 OID 34532)
-- Name: status_rastreio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.status_rastreio
    ADD CONSTRAINT status_rastreio_pkey PRIMARY KEY (id);


--
-- TOC entry 2154 (class 2606 OID 34545)
-- Name: unique_acessso_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios_acessos
    ADD CONSTRAINT unique_acessso_user UNIQUE (usuario_id, acesso_id);


--
-- TOC entry 2152 (class 2606 OID 34540)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2156 (class 2606 OID 34552)
-- Name: venda_compra_loja_virtual_teste_nota_fiscal_venda_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT venda_compra_loja_virtual_teste_nota_fiscal_venda_id_key UNIQUE (nota_fiscal_venda_id);


--
-- TOC entry 2158 (class 2606 OID 34550)
-- Name: venda_compra_loja_virtual_teste_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT venda_compra_loja_virtual_teste_pkey PRIMARY KEY (id);


--
-- TOC entry 2179 (class 2620 OID 34635)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE UPDATE ON public.endereco FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2175 (class 2620 OID 34636)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2176 (class 2620 OID 34637)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2178 (class 2620 OID 34641)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.conta_receber FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2180 (class 2620 OID 34642)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.imagem_produto FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2181 (class 2620 OID 34643)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.item_venda_loja FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2182 (class 2620 OID 34644)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2183 (class 2620 OID 34645)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.nota_item_produto FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2184 (class 2620 OID 34646)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.usuario FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2185 (class 2620 OID 34647)
-- Name: trg_valida_pessoa_endereco; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco BEFORE INSERT OR UPDATE ON public.venda_compra_loja_virtual_teste FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco();


--
-- TOC entry 2177 (class 2620 OID 34640)
-- Name: trg_valida_pessoa_endereco2; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_valida_pessoa_endereco2 BEFORE INSERT OR UPDATE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE public.valida_pessoa_em_endereco2();


--
-- TOC entry 2168 (class 2606 OID 34598)
-- Name: acesso_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios_acessos
    ADD CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES public.acesso(id);


--
-- TOC entry 2163 (class 2606 OID 34573)
-- Name: conta_pagar_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_fiscal_compra
    ADD CONSTRAINT conta_pagar_fk FOREIGN KEY (conta_pagar_id) REFERENCES public.conta_pagar(id);


--
-- TOC entry 2170 (class 2606 OID 34608)
-- Name: cupom_desconto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT cupom_desconto_fk FOREIGN KEY (cupom_desconto_id) REFERENCES public.cupom_desconto(id);


--
-- TOC entry 2171 (class 2606 OID 34613)
-- Name: enderecocobranca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT enderecocobranca_fk FOREIGN KEY (endereco_cobranca_id) REFERENCES public.endereco(id);


--
-- TOC entry 2172 (class 2606 OID 34618)
-- Name: enderecoentrega_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT enderecoentrega_fk FOREIGN KEY (endereco_entrega_id) REFERENCES public.endereco(id);


--
-- TOC entry 2173 (class 2606 OID 34623)
-- Name: forma_pagamento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT forma_pagamento_fk FOREIGN KEY (form_pagamento_id) REFERENCES public.forma_pagamento(id);


--
-- TOC entry 2165 (class 2606 OID 34583)
-- Name: nota_fiscal_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_item_produto
    ADD CONSTRAINT nota_fiscal_compra_fk FOREIGN KEY (nota_fiscal_compra_id) REFERENCES public.nota_fiscal_compra(id);


--
-- TOC entry 2174 (class 2606 OID 34628)
-- Name: nota_fiscal_venda_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_compra_loja_virtual_teste
    ADD CONSTRAINT nota_fiscal_venda_fk FOREIGN KEY (nota_fiscal_venda_id) REFERENCES public.nota_fiscal_venda(id);


--
-- TOC entry 2159 (class 2606 OID 34553)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.avaliacao_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2160 (class 2606 OID 34558)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.imagem_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2161 (class 2606 OID 34563)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_venda_loja
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2166 (class 2606 OID 34588)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_item_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2169 (class 2606 OID 34603)
-- Name: usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios_acessos
    ADD CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2162 (class 2606 OID 34568)
-- Name: venda_compra_loja_virtual_teste_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_venda_loja
    ADD CONSTRAINT venda_compra_loja_virtual_teste_fk FOREIGN KEY (venda_compra_loja_virtual_teste_id) REFERENCES public.venda_compra_loja_virtual_teste(id);


--
-- TOC entry 2164 (class 2606 OID 34578)
-- Name: venda_compra_loja_virtual_teste_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nota_fiscal_venda
    ADD CONSTRAINT venda_compra_loja_virtual_teste_fk FOREIGN KEY (venda_compra_loja_virtual_teste_id) REFERENCES public.venda_compra_loja_virtual_teste(id);


--
-- TOC entry 2167 (class 2606 OID 34593)
-- Name: venda_compra_loja_virtual_teste_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.status_rastreio
    ADD CONSTRAINT venda_compra_loja_virtual_teste_fk FOREIGN KEY (venda_compra_loja_virtual_teste_id) REFERENCES public.venda_compra_loja_virtual_teste(id);


--
-- TOC entry 2347 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2026-02-02 15:14:46

--
-- PostgreSQL database dump complete
--

