--
-- PostgreSQL database dump
--

\restrict YbDSmWHXonEVMbtFwIAThm3QesdfumAjTpyPAIrkm5RuAXdc7BUXnw3x6IAOjmT

-- Dumped from database version 18.3 (Debian 18.3-1.pgdg13+1)
-- Dumped by pg_dump version 18.3 (Debian 18.3-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: tipo_tirocinio_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_tirocinio_enum AS ENUM (
    'STAGE',
    'ALTO_APPRENDISTATO'
);


ALTER TYPE public.tipo_tirocinio_enum OWNER TO postgres;

--
-- PostgreSQL database dump complete
--

\unrestrict YbDSmWHXonEVMbtFwIAThm3QesdfumAjTpyPAIrkm5RuAXdc7BUXnw3x6IAOjmT

