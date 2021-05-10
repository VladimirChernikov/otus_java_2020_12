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

--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: usr
--

COPY public.client (id, version, name) FROM stdin;
1	1	client 1
51	1	client 2
101	1	client 3
102	1	client 4
103	1	client 5
\.


--
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: usr
--

COPY public.address (id, version, street, client_id) FROM stdin;
1	1	Address 4	102
2	1	Address 5	103
\.


--
-- Data for Name: phone; Type: TABLE DATA; Schema: public; Owner: usr
--

COPY public.phone (id, version, number, client_id) FROM stdin;
1	1	1234	102
2	1	5678	102
3	1	1234	103
4	1	5678	103
\.


--
-- Name: address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: usr
--

SELECT pg_catalog.setval('public.address_id_seq', 51, true);


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: usr
--

SELECT pg_catalog.setval('public.client_id_seq', 151, true);


--
-- Name: phone_id_seq; Type: SEQUENCE SET; Schema: public; Owner: usr
--

SELECT pg_catalog.setval('public.phone_id_seq', 51, true);



--
-- PostgreSQL database dump complete
--

