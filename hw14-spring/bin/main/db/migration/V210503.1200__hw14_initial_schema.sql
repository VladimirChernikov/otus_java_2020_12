--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2 (Debian 13.2-1.pgdg100+1)
-- Dumped by pg_dump version 13.2 (Debian 13.2-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


--
-- Name: address; Type: TABLE; Schema: public; Owner: usr
--

CREATE TABLE public.address (
    id bigserial NOT NULL,
    version bigint NOT NULL,
    street character varying(255),
    client_id bigint NOT NULL
);


ALTER TABLE public.address OWNER TO usr;

--
-- Name: client; Type: TABLE; Schema: public; Owner: usr
--

CREATE TABLE public.client (
    id bigserial NOT NULL,
    version bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.client OWNER TO usr;


--
-- Name: phone; Type: TABLE; Schema: public; Owner: usr
--

CREATE TABLE public.phone (
    id bigserial NOT NULL,
    version bigint NOT NULL,
    number character varying(255),
    client_id bigint NOT NULL
);


ALTER TABLE public.phone OWNER TO usr;


--
-- Name: address address_pkey; Type: CONSTRAINT; Schema: public; Owner: usr
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: usr
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: phone phone_pkey; Type: CONSTRAINT; Schema: public; Owner: usr
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_pkey PRIMARY KEY (id);





--
-- Name: phone fk3o48ec26lujl3kf01hwqplhn2; Type: FK CONSTRAINT; Schema: public; Owner: usr
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT fk3o48ec26lujl3kf01hwqplhn2 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: address fk7156ty2o5atyuy9f6kuup9dna; Type: FK CONSTRAINT; Schema: public; Owner: usr
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT fk7156ty2o5atyuy9f6kuup9dna FOREIGN KEY (client_id) REFERENCES public.client(id);





--
-- PostgreSQL database dump complete
--

