--
-- PostgreSQL database dump
--

\restrict 8O9JcZEYbJGhbyd7RcqCeYh8jiQxpsnPT6bSUksha0f4pWlw0cecYzNOTKnhDra

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-03-28 22:22:36

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
-- TOC entry 876 (class 1247 OID 25737)
-- Name: esito_colloquio; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.esito_colloquio AS ENUM (
    'Positivo',
    'Negativo',
    'In attesa'
);


ALTER TYPE public.esito_colloquio OWNER TO postgres;

--
-- TOC entry 879 (class 1247 OID 25744)
-- Name: esito_tirocinio; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.esito_tirocinio AS ENUM (
    'In corso',
    'Concluso',
    'Interrotto'
);


ALTER TYPE public.esito_tirocinio OWNER TO postgres;

--
-- TOC entry 936 (class 1247 OID 26046)
-- Name: ruolo_contatto_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.ruolo_contatto_enum AS ENUM (
    'HR',
    'Tutor IT',
    'Manager',
    'AI Specialist',
    'Data Analyst'
);


ALTER TYPE public.ruolo_contatto_enum OWNER TO postgres;

--
-- TOC entry 882 (class 1247 OID 25752)
-- Name: stato_esito; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stato_esito AS ENUM (
    'Positivo',
    'Negativo',
    'In attesa',
    'Approvato',
    'Rifiutato'
);


ALTER TYPE public.stato_esito OWNER TO postgres;

--
-- TOC entry 885 (class 1247 OID 25760)
-- Name: tipo_documento; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_documento AS ENUM (
    'Convenzione',
    'PFI',
    'DPA',
    'Scheda mensile',
    'Calendario'
);


ALTER TYPE public.tipo_documento OWNER TO postgres;

--
-- TOC entry 888 (class 1247 OID 25772)
-- Name: tipo_documento_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_documento_enum AS ENUM (
    'Convenzione',
    'PFI',
    'DPA',
    'Scheda mensile',
    'Calendario',
    'Progetto Formativo',
    'Registro Presenze',
    'Valutazione Finale'
);


ALTER TYPE public.tipo_documento_enum OWNER TO postgres;

--
-- TOC entry 927 (class 1247 OID 25975)
-- Name: tipo_responsabile_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_responsabile_enum AS ENUM (
    'Coordinatore',
    'Responsabile PCTO',
    'Tutor Scolastico',
    'Direttore'
);


ALTER TYPE public.tipo_responsabile_enum OWNER TO postgres;

--
-- TOC entry 891 (class 1247 OID 25784)
-- Name: tipo_tirocinio; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_tirocinio AS ENUM (
    'Curricolare',
    'Extra-curricolare'
);


ALTER TYPE public.tipo_tirocinio OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 25789)
-- Name: allievo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.allievo (
    id integer NOT NULL,
    nome character varying(50),
    cognome character varying(50),
    codice_fiscale character varying(20),
    corso_id integer,
    note text,
    data_di_nascita date,
    id_utente integer
);


ALTER TABLE public.allievo OWNER TO postgres;

--
-- TOC entry 5185 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN allievo.id_utente; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.allievo.id_utente IS 'Collegamento all account di accesso dell allievo';


--
-- TOC entry 220 (class 1259 OID 25795)
-- Name: allievo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.allievo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.allievo_id_seq OWNER TO postgres;

--
-- TOC entry 5186 (class 0 OID 0)
-- Dependencies: 220
-- Name: allievo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.allievo_id_seq OWNED BY public.allievo.id;


--
-- TOC entry 221 (class 1259 OID 25796)
-- Name: azienda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.azienda (
    id integer NOT NULL,
    ragione_sociale character varying(100),
    partita_iva character varying(20),
    telefono character varying(20),
    email character varying(100),
    indirizzo character varying(150),
    cap character varying(10)
);


ALTER TABLE public.azienda OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 25800)
-- Name: azienda_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.azienda_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.azienda_id_seq OWNER TO postgres;

--
-- TOC entry 5187 (class 0 OID 0)
-- Dependencies: 222
-- Name: azienda_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.azienda_id_seq OWNED BY public.azienda.id;


--
-- TOC entry 223 (class 1259 OID 25801)
-- Name: caso_critico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.caso_critico (
    id integer NOT NULL,
    allievo_id integer NOT NULL,
    data_segnalazione date DEFAULT CURRENT_DATE,
    tipo_criticita character varying(250),
    descrizione text,
    risolto boolean DEFAULT false
);


ALTER TABLE public.caso_critico OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 25810)
-- Name: caso_critico_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.caso_critico_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.caso_critico_id_seq OWNER TO postgres;

--
-- TOC entry 5188 (class 0 OID 0)
-- Dependencies: 224
-- Name: caso_critico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.caso_critico_id_seq OWNED BY public.caso_critico.id;


--
-- TOC entry 243 (class 1259 OID 26018)
-- Name: colloquio_tirocinio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.colloquio_tirocinio (
    id integer NOT NULL,
    allievo_id integer NOT NULL,
    azienda_id integer NOT NULL,
    data_colloquio date NOT NULL,
    tipo_evento character varying(100),
    esito public.stato_esito DEFAULT 'In attesa'::public.stato_esito,
    note_feedback text
);


ALTER TABLE public.colloquio_tirocinio OWNER TO postgres;

--
-- TOC entry 5189 (class 0 OID 0)
-- Dependencies: 243
-- Name: TABLE colloquio_tirocinio; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.colloquio_tirocinio IS 'Ristrutturazione tabella: ogni riga rappresenta un singolo incontro (Relazione 1:N)';


--
-- TOC entry 5190 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN colloquio_tirocinio.tipo_evento; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.colloquio_tirocinio.tipo_evento IS 'Descrive la fase del colloquio (es. Step 1, Step 2)';


--
-- TOC entry 242 (class 1259 OID 26017)
-- Name: colloquio_tirocinio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.colloquio_tirocinio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.colloquio_tirocinio_id_seq OWNER TO postgres;

--
-- TOC entry 5191 (class 0 OID 0)
-- Dependencies: 242
-- Name: colloquio_tirocinio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.colloquio_tirocinio_id_seq OWNED BY public.colloquio_tirocinio.id;


--
-- TOC entry 225 (class 1259 OID 25818)
-- Name: contatto_aziendale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contatto_aziendale (
    id integer NOT NULL,
    azienda_id integer,
    nome character varying(50),
    cognome character varying(50),
    ruolo public.ruolo_contatto_enum,
    telefono character varying(20),
    email character varying(100)
);


ALTER TABLE public.contatto_aziendale OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 25822)
-- Name: contatto_aziendale_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatto_aziendale_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.contatto_aziendale_id_seq OWNER TO postgres;

--
-- TOC entry 5192 (class 0 OID 0)
-- Dependencies: 226
-- Name: contatto_aziendale_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatto_aziendale_id_seq OWNED BY public.contatto_aziendale.id;


--
-- TOC entry 227 (class 1259 OID 25823)
-- Name: corso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.corso (
    id integer NOT NULL,
    nome_corso character varying(100),
    anno_accademico character varying(20),
    stato character varying(20)
);


ALTER TABLE public.corso OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 25827)
-- Name: corso_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.corso_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.corso_id_seq OWNER TO postgres;

--
-- TOC entry 5193 (class 0 OID 0)
-- Dependencies: 228
-- Name: corso_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.corso_id_seq OWNED BY public.corso.id;


--
-- TOC entry 229 (class 1259 OID 25828)
-- Name: documento_tirocinio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documento_tirocinio (
    id integer NOT NULL,
    tirocinio_id integer,
    tipo_documento public.tipo_documento_enum,
    presente boolean,
    data_acquisizione date
);


ALTER TABLE public.documento_tirocinio OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 25834)
-- Name: documento_tirocinio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.documento_tirocinio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.documento_tirocinio_id_seq OWNER TO postgres;

--
-- TOC entry 5194 (class 0 OID 0)
-- Dependencies: 230
-- Name: documento_tirocinio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.documento_tirocinio_id_seq OWNED BY public.documento_tirocinio.id;


--
-- TOC entry 231 (class 1259 OID 25835)
-- Name: monitoraggio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.monitoraggio (
    id integer NOT NULL,
    tirocinio_id integer,
    data_monitoraggio date,
    responsabile integer,
    note text
);


ALTER TABLE public.monitoraggio OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 25841)
-- Name: monitoraggio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.monitoraggio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.monitoraggio_id_seq OWNER TO postgres;

--
-- TOC entry 5195 (class 0 OID 0)
-- Dependencies: 232
-- Name: monitoraggio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.monitoraggio_id_seq OWNED BY public.monitoraggio.id;


--
-- TOC entry 241 (class 1259 OID 25984)
-- Name: responsabile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.responsabile (
    id integer NOT NULL,
    nome character varying(50) NOT NULL,
    cognome character varying(50) NOT NULL,
    codice_fiscale character(16),
    email character varying(100) NOT NULL,
    telefono character varying(20),
    tipo public.tipo_responsabile_enum,
    id_utente integer,
    attivo boolean DEFAULT true
);


ALTER TABLE public.responsabile OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 25983)
-- Name: responsabile_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.responsabile_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.responsabile_id_seq OWNER TO postgres;

--
-- TOC entry 5196 (class 0 OID 0)
-- Dependencies: 240
-- Name: responsabile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.responsabile_id_seq OWNED BY public.responsabile.id;


--
-- TOC entry 233 (class 1259 OID 25842)
-- Name: ruolo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ruolo (
    id_ruolo integer NOT NULL,
    codice character varying(50) NOT NULL,
    descrizione character varying(255)
);


ALTER TABLE public.ruolo OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 25847)
-- Name: ruolo_id_ruolo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ruolo_id_ruolo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ruolo_id_ruolo_seq OWNER TO postgres;

--
-- TOC entry 5197 (class 0 OID 0)
-- Dependencies: 234
-- Name: ruolo_id_ruolo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ruolo_id_ruolo_seq OWNED BY public.ruolo.id_ruolo;


--
-- TOC entry 235 (class 1259 OID 25848)
-- Name: tirocinio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tirocinio (
    id integer NOT NULL,
    allievo_id integer,
    azienda_id integer,
    data_inizio date,
    data_fine date,
    tipo character varying(50),
    esito public.stato_esito,
    frequenza character varying(50)
);


ALTER TABLE public.tirocinio OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 25852)
-- Name: tirocinio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tirocinio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tirocinio_id_seq OWNER TO postgres;

--
-- TOC entry 5198 (class 0 OID 0)
-- Dependencies: 236
-- Name: tirocinio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tirocinio_id_seq OWNED BY public.tirocinio.id;


--
-- TOC entry 237 (class 1259 OID 25853)
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    id_utente integer NOT NULL,
    email character varying(255) NOT NULL,
    username character varying(100),
    password_hash character varying(255) NOT NULL,
    id_ruolo integer NOT NULL,
    attivo boolean DEFAULT true NOT NULL,
    creato_il timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    aggiornato_il timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ultimo_accesso timestamp without time zone
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 25868)
-- Name: utente_id_utente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utente_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utente_id_utente_seq OWNER TO postgres;

--
-- TOC entry 5199 (class 0 OID 0)
-- Dependencies: 238
-- Name: utente_id_utente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utente_id_utente_seq OWNED BY public.utente.id_utente;


--
-- TOC entry 239 (class 1259 OID 25869)
-- Name: vista_allievi_senza_stage; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vista_allievi_senza_stage AS
 SELECT a.id,
    a.nome,
    a.cognome,
    c.nome_corso
   FROM ((public.allievo a
     LEFT JOIN public.tirocinio t ON ((a.id = t.allievo_id)))
     LEFT JOIN public.corso c ON ((a.corso_id = c.id)))
  WHERE (t.id IS NULL);


ALTER VIEW public.vista_allievi_senza_stage OWNER TO postgres;

--
-- TOC entry 4939 (class 2604 OID 25963)
-- Name: allievo id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.allievo ALTER COLUMN id SET DEFAULT nextval('public.allievo_id_seq'::regclass);


--
-- TOC entry 4940 (class 2604 OID 25964)
-- Name: azienda id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.azienda ALTER COLUMN id SET DEFAULT nextval('public.azienda_id_seq'::regclass);


--
-- TOC entry 4941 (class 2604 OID 25965)
-- Name: caso_critico id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caso_critico ALTER COLUMN id SET DEFAULT nextval('public.caso_critico_id_seq'::regclass);


--
-- TOC entry 4956 (class 2604 OID 26021)
-- Name: colloquio_tirocinio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colloquio_tirocinio ALTER COLUMN id SET DEFAULT nextval('public.colloquio_tirocinio_id_seq'::regclass);


--
-- TOC entry 4944 (class 2604 OID 25967)
-- Name: contatto_aziendale id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contatto_aziendale ALTER COLUMN id SET DEFAULT nextval('public.contatto_aziendale_id_seq'::regclass);


--
-- TOC entry 4945 (class 2604 OID 25968)
-- Name: corso id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.corso ALTER COLUMN id SET DEFAULT nextval('public.corso_id_seq'::regclass);


--
-- TOC entry 4946 (class 2604 OID 25969)
-- Name: documento_tirocinio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_tirocinio ALTER COLUMN id SET DEFAULT nextval('public.documento_tirocinio_id_seq'::regclass);


--
-- TOC entry 4947 (class 2604 OID 25970)
-- Name: monitoraggio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.monitoraggio ALTER COLUMN id SET DEFAULT nextval('public.monitoraggio_id_seq'::regclass);


--
-- TOC entry 4954 (class 2604 OID 25987)
-- Name: responsabile id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsabile ALTER COLUMN id SET DEFAULT nextval('public.responsabile_id_seq'::regclass);


--
-- TOC entry 4948 (class 2604 OID 25971)
-- Name: ruolo id_ruolo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruolo ALTER COLUMN id_ruolo SET DEFAULT nextval('public.ruolo_id_ruolo_seq'::regclass);


--
-- TOC entry 4949 (class 2604 OID 25972)
-- Name: tirocinio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio ALTER COLUMN id SET DEFAULT nextval('public.tirocinio_id_seq'::regclass);


--
-- TOC entry 4950 (class 2604 OID 25973)
-- Name: utente id_utente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente ALTER COLUMN id_utente SET DEFAULT nextval('public.utente_id_utente_seq'::regclass);


--
-- TOC entry 5156 (class 0 OID 25789)
-- Dependencies: 219
-- Data for Name: allievo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.allievo (id, nome, cognome, codice_fiscale, corso_id, note, data_di_nascita, id_utente) FROM stdin;
3	Sara	Verdi	VRDSRA02C03TO	2	\N	2002-11-10	\N
4	Elena	Neri	NRELE00D04TO	3	\N	2000-08-30	\N
5	Said	Benaly	BNLSID01E05TO	1	\N	2001-02-14	\N
6	Giulia	Gialli	GLLGLI00A01TO	3	\N	2000-02-10	\N
7	Davide	Blu	BLUDVD01B02TO	4	\N	2001-06-15	\N
8	Chiara	Arancio	ARNCRA02C03TO	5	\N	2002-12-01	\N
9	Matteo	Viola	VLAMTT00D04TO	6	\N	2000-09-20	\N
10	Sofia	Rosa	RSASFA01E05TO	7	\N	2001-03-12	\N
11	Alessandro	Marroni	MRRLSS02F06TO	8	\N	2002-05-25	\N
12	Beatrice	Grigi	GRGBTC00G07TO	9	\N	2000-11-14	\N
13	Riccardo	Neri	NRERCC01H08TO	10	\N	2001-07-08	\N
14	Francesca	Bianchi	BNCFNC02I09TO	11	\N	2002-01-30	\N
15	Emanuele	Verdi	VRDMNL00L10TO	12	\N	2000-04-18	\N
16	Giulia	Gialli	GLLGLI00A01TO	3	\N	2000-02-10	\N
17	Davide	Blu	BLUDVD01B02TO	4	\N	2001-06-15	\N
18	Chiara	Arancio	ARNCRA02C03TO	5	\N	2002-12-01	\N
19	Matteo	Viola	VLAMTT00D04TO	6	\N	2000-09-20	\N
20	Sofia	Rosa	RSASFA01E05TO	7	\N	2001-03-12	\N
21	Alessandro	Marroni	MRRLSS02F06TO	8	\N	2002-05-25	\N
22	Beatrice	Grigi	GRGBTC00G07TO	9	\N	2000-11-14	\N
23	Riccardo	Neri	NRERCC01H08TO	10	\N	2001-07-08	\N
24	Francesca	Bianchi	BNCFNC02I09TO	11	\N	2002-01-30	\N
25	Emanuele	Verdi	VRDMNL00L10TO	12	\N	2000-04-18	\N
1	Mario	Rossi	RSSMRA00A01TO	1	\N	2000-01-15	2
2	Luca	Bianchi	BNCLCU01B02TO	1	\N	2001-05-20	3
\.


--
-- TOC entry 5158 (class 0 OID 25796)
-- Dependencies: 221
-- Data for Name: azienda; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.azienda (id, ragione_sociale, partita_iva, telefono, email, indirizzo, cap) FROM stdin;
1	Tech Solutions Srl	12345678901	011123456	info@techsolutions.it	Via Roma 10, Torino	10121
2	Innovate IT Spa	09876543210	011654321	hr@innovate.it	Corso Francia 50, Torino	10138
3	Data Systems Group	11223344556	011998877	jobs@datasys.it	Via Milano 5, Milano	20121
4	Cloud Services	66554433221	011443322	tirocini@cloudserv.it	Piazza Castello 1, Torino	10123
5	Web Agency Torino	77889900112	011221133	admin@webagency.it	Via Po 12, Torino	10124
6	Global Tech Spa	01234567890	011555111	hr@globaltech.com	Via Roma 1, Torino	10121
7	Cyber Security SRL	09876543211	011555222	info@cybersec.it	Corso Unione 10, Torino	10134
8	Web Factory	11223344557	011555333	jobs@webfactory.it	Via Milano 20, Milano	20100
9	Software House Torino	44556677889	011555444	staff@swtorino.it	Via Po 5, Torino	10124
10	Cloud Solutions	99887766554	011555555	recruiting@cloudsol.it	Via Dante 3, Milano	20121
11	Digital Agency SRL	77665544332	011555666	hello@digitalagency.it	Piazza Castello 15, Torino	10123
12	Future IT Spa	12312312312	011555777	hr@futureit.it	Via Nizza 100, Torino	10126
13	E-commerce Experts	32132132132	011555888	team@ecommerce.it	Corso Re Umberto 2, Torino	10121
14	AI Innovations	45645645645	011555999	lab@aiinnovations.it	Via Lagrange 7, Torino	10123
15	Data Mining Group	78978978978	011555000	data@mining.it	Via Garibaldi 12, Torino	10122
16	NaNo Sistemi	08254840011	011 7072021	info@nanosistemi.com	via Livorno 60	10146
17	Telematica Informatica	11223344556	011 1234567	info@telematicainformatica.it	C.so Galileo Ferraris 77	10148
19	EuroSoftware	01234567890	011 5566778	info@eurosoftware.it	Piazza del Monastero 15b	10122
20	3E Informatica	09876543210	011 4433221	info@3einformatica.it	Corso Galileo Ferraris 2	10135
21	TeamWare Informatica	09632140019	011 4031234	info@teamwarenet.it	C.so Giulio Cesare 338/26	10093
22	Oasi Informatica	05432100012	011 7805643	aiuto@oasi.ws	Via Gianfrancesco Re 28	10095
23	Graynos	08877665544	011 7801122	info@graynos.com	via Schiaparelli 16	10095
24	Euri Group	03344556677	011 3112233	hr@eurigroup.com	Via Paolo Borsellino, 38/16	10141
25	Dedagroup	02233445566	011 2481020	info@dedagroup.it	C.so Francesco Ferrucci, 112	10155
26	Si-Soft Informatica	01122334455	011 3456789	risorseumane@si-soft.org	Corso Unione Sovietica, 612/21	10134
27	WillBit	04455667788	011 3133211	info@willbit.com	Via Alessandro Antonelli, 10, Collegno TO	10135
28	TESEO informatica	05566778899	011 4332211	info@teseoinformatica.it	Via Goito, 51, 10095 Grugliasco TO	10149
18	M.C. Engineering Srl	07654321001	011 9876543	hr@eurigroup.com	Str. Torino, 43, 10093 Orbassano TO	10138
\.


--
-- TOC entry 5160 (class 0 OID 25801)
-- Dependencies: 223
-- Data for Name: caso_critico; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.caso_critico (id, allievo_id, data_segnalazione, tipo_criticita, descrizione, risolto) FROM stdin;
1	8	2025-06-05	Comunicazione	Difficoltà a comunicare con il tutor aziendale.	t
2	14	2025-09-05	Competenze	Lo studente non ha le basi di SQL richieste.	f
3	10	2025-07-10	Trasporto	Difficoltà a raggiungere la sede operativa.	t
\.


--
-- TOC entry 5179 (class 0 OID 26018)
-- Dependencies: 243
-- Data for Name: colloquio_tirocinio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.colloquio_tirocinio (id, allievo_id, azienda_id, data_colloquio, tipo_evento, esito, note_feedback) FROM stdin;
1	2	1	2024-03-05	Conoscitivo	Approvato	Candidato molto educato e puntuale.
2	2	1	2024-03-12	Test Logico	Approvato	Punteggio 95/100, eccellente ragionamento.
3	2	1	2024-03-20	Colloquio Finale	Approvato	Confermato per lo stage estivo.
4	3	2	2024-03-08	Primo Incontro	Rifiutato	Mancanza di competenze tecniche richieste in Java.
5	4	2	2024-03-15	Colloquio Tecnico	In attesa	Il tutor tecnico deve ancora dare il responso.
6	5	3	2024-03-18	Intervista Unica	Approvato	Profilo perfetto per le esigenze aziendali.
7	6	1	2024-03-21	Colloquio HR	In attesa	Attesa di disponibilità per il secondo step.
8	4	3	2024-03-22	Primo Step	Rifiutato	Troppo lontano dalla sede operativa.
9	2	2	2024-03-23	Colloquio Conoscitivo	Approvato	Persona intraprendente, buona conoscenza SQL.
10	1	3	2024-03-25	Recruiting Day	In attesa	Interesse mostrato per lo sviluppo Front-end.
\.


--
-- TOC entry 5162 (class 0 OID 25818)
-- Dependencies: 225
-- Data for Name: contatto_aziendale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contatto_aziendale (id, azienda_id, nome, cognome, ruolo, telefono, email) FROM stdin;
1	6	Luca	Santi	HR	011000666	l.santi@digitalagency.it
2	7	Marta	Ferri	Tutor IT	011000777	m.ferri@futureit.it
3	8	Paolo	Riva	Manager	011000888	p.riva@ecommerce.it
4	9	Laura	Bosco	AI Specialist	011000999	l.bosco@aiinnovations.it
5	10	Stefano	Luce	Data Analyst	011000000	s.luce@mining.it
6	6	Luca	Santi	HR	011000666	l.santi@digitalagency.it
7	7	Marta	Ferri	Tutor IT	011000777	m.ferri@futureit.it
8	8	Paolo	Riva	Manager	011000888	p.riva@ecommerce.it
9	9	Laura	Bosco	AI Specialist	011000999	l.bosco@aiinnovations.it
10	10	Stefano	Luce	Data Analyst	011000000	s.luce@mining.it
\.


--
-- TOC entry 5164 (class 0 OID 25823)
-- Dependencies: 227
-- Data for Name: corso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.corso (id, nome_corso, anno_accademico, stato) FROM stdin;
1	Cloud Developer	2024-2026	In corso
2	Cybersecurity Specialist	2023-2025	In corso
3	Data Analyst	2024-2026	In corso
4	Software Architect	2023-2025	Concluso
5	Digital Strategy	2024-2026	In corso
6	Cloud Developer	2024-2026	In corso
7	Cybersecurity Specialist	2023-2025	In corso
8	Data Analyst	2024-2026	In corso
9	Software Architect	2023-2025	Concluso
10	Digital Strategy	2024-2026	In corso
11	Cloud Developer	2024-2026	In corso
12	Cybersecurity Specialist	2023-2025	In corso
13	Data Analyst	2024-2026	In corso
14	Software Architect	2023-2025	Concluso
15	Digital Strategy	2024-2026	In corso
16	Backend Developer	2024-2026	In corso
17	Frontend Specialist	2024-2026	In corso
18	AI & Machine Learning	2023-2025	In corso
19	System Administrator	2023-2025	Concluso
20	Project Manager ICT	2024-2026	In corso
21	Social Media Manager	2023-2025	In corso
22	Big Data Architect	2024-2026	In corso
23	Mobile Developer	2023-2025	Concluso
24	UI/UX Designer	2024-2026	In corso
25	Blockchain Expert	2024-2026	In corso
\.


--
-- TOC entry 5166 (class 0 OID 25828)
-- Dependencies: 229
-- Data for Name: documento_tirocinio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.documento_tirocinio (id, tirocinio_id, tipo_documento, presente, data_acquisizione) FROM stdin;
24	1	Progetto Formativo	t	2024-01-10
25	1	Registro Presenze	t	2024-02-15
27	2	Progetto Formativo	t	2024-01-12
29	2	Valutazione Finale	t	2024-03-20
26	1	Valutazione Finale	f	2024-02-17
28	2	Registro Presenze	f	2024-01-26
\.


--
-- TOC entry 5168 (class 0 OID 25835)
-- Dependencies: 231
-- Data for Name: monitoraggio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.monitoraggio (id, tirocinio_id, data_monitoraggio, responsabile, note) FROM stdin;
\.


--
-- TOC entry 5177 (class 0 OID 25984)
-- Dependencies: 241
-- Data for Name: responsabile; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.responsabile (id, nome, cognome, codice_fiscale, email, telefono, tipo, id_utente, attivo) FROM stdin;
1	Giuseppe	Verdi	VRDGPP80A01H501X	g.verdi@scuola.it	011-5556677	Responsabile PCTO	6	t
2	Francesca	Neri	NREFRC75B42L219Z	f.neri@scuola.it	011-5558899	Responsabile PCTO	7	t
\.


--
-- TOC entry 5170 (class 0 OID 25842)
-- Dependencies: 233
-- Data for Name: ruolo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ruolo (id_ruolo, codice, descrizione) FROM stdin;
1	ADMIN	Amministratore del sistema
2	ALLIEVO	Utente allievo
3	REFERENTE_AZIENDA	Contatto/referente aziendale
4	DOCENTE	Docente o formatore
\.


--
-- TOC entry 5172 (class 0 OID 25848)
-- Dependencies: 235
-- Data for Name: tirocinio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tirocinio (id, allievo_id, azienda_id, data_inizio, data_fine, tipo, esito, frequenza) FROM stdin;
1	1	1	2025-04-01	2025-07-01	Curricolare	In attesa	Full-time
2	3	3	2025-04-15	2025-07-15	Curricolare	In attesa	Full-time
\.


--
-- TOC entry 5174 (class 0 OID 25853)
-- Dependencies: 237
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utente (id_utente, email, username, password_hash, id_ruolo, attivo, creato_il, aggiornato_il, ultimo_accesso) FROM stdin;
1	admin@scuola.it	admin_boss	hash_secure_123	1	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
2	mario.rossi@studente.it	mrossi88	hash_psw_99	2	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
3	luca.bianchi@studente.it	lbianchi01	hash_psw_88	2	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
4	sara.verdi@studente.it	sverdi_99	hash_psw_77	2	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
5	youssef.maroc@studente.it	ymaroc20	hash_psw_66	2	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
6	g.verdi@scuola.it	gverdi_resp	hash_resp_11	3	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
7	f.neri@scuola.it	fneri_coordinatore	hash_resp_22	3	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
8	tech@azienda1.it	tutor_tech1	hash_tutor_55	4	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
9	hr@azienda2.it	hr_tutor2	hash_tutor_44	4	t	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
10	info@azienda3.it	info_dev	hash_tutor_33	4	f	2026-03-27 13:32:49.992029	2026-03-27 13:32:49.992029	\N
\.


--
-- TOC entry 5200 (class 0 OID 0)
-- Dependencies: 220
-- Name: allievo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.allievo_id_seq', 25, true);


--
-- TOC entry 5201 (class 0 OID 0)
-- Dependencies: 222
-- Name: azienda_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.azienda_id_seq', 28, true);


--
-- TOC entry 5202 (class 0 OID 0)
-- Dependencies: 224
-- Name: caso_critico_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.caso_critico_id_seq', 3, true);


--
-- TOC entry 5203 (class 0 OID 0)
-- Dependencies: 242
-- Name: colloquio_tirocinio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.colloquio_tirocinio_id_seq', 10, true);


--
-- TOC entry 5204 (class 0 OID 0)
-- Dependencies: 226
-- Name: contatto_aziendale_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatto_aziendale_id_seq', 10, true);


--
-- TOC entry 5205 (class 0 OID 0)
-- Dependencies: 228
-- Name: corso_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.corso_id_seq', 25, true);


--
-- TOC entry 5206 (class 0 OID 0)
-- Dependencies: 230
-- Name: documento_tirocinio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.documento_tirocinio_id_seq', 29, true);


--
-- TOC entry 5207 (class 0 OID 0)
-- Dependencies: 232
-- Name: monitoraggio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.monitoraggio_id_seq', 10, true);


--
-- TOC entry 5208 (class 0 OID 0)
-- Dependencies: 240
-- Name: responsabile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.responsabile_id_seq', 2, true);


--
-- TOC entry 5209 (class 0 OID 0)
-- Dependencies: 234
-- Name: ruolo_id_ruolo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ruolo_id_ruolo_seq', 4, true);


--
-- TOC entry 5210 (class 0 OID 0)
-- Dependencies: 236
-- Name: tirocinio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tirocinio_id_seq', 2, true);


--
-- TOC entry 5211 (class 0 OID 0)
-- Dependencies: 238
-- Name: utente_id_utente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utente_id_utente_seq', 10, true);


--
-- TOC entry 4959 (class 2606 OID 26062)
-- Name: allievo allievo_id_utente_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.allievo
    ADD CONSTRAINT allievo_id_utente_key UNIQUE (id_utente);


--
-- TOC entry 4961 (class 2606 OID 25886)
-- Name: allievo allievo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.allievo
    ADD CONSTRAINT allievo_pkey PRIMARY KEY (id);


--
-- TOC entry 4963 (class 2606 OID 25888)
-- Name: azienda azienda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (id);


--
-- TOC entry 4965 (class 2606 OID 25890)
-- Name: caso_critico caso_critico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caso_critico
    ADD CONSTRAINT caso_critico_pkey PRIMARY KEY (id);


--
-- TOC entry 4995 (class 2606 OID 26030)
-- Name: colloquio_tirocinio colloquio_tirocinio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colloquio_tirocinio
    ADD CONSTRAINT colloquio_tirocinio_pkey PRIMARY KEY (id);


--
-- TOC entry 4967 (class 2606 OID 25894)
-- Name: contatto_aziendale contatto_aziendale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contatto_aziendale
    ADD CONSTRAINT contatto_aziendale_pkey PRIMARY KEY (id);


--
-- TOC entry 4969 (class 2606 OID 25896)
-- Name: corso corso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.corso
    ADD CONSTRAINT corso_pkey PRIMARY KEY (id);


--
-- TOC entry 4971 (class 2606 OID 25898)
-- Name: documento_tirocinio documento_tirocinio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_tirocinio
    ADD CONSTRAINT documento_tirocinio_pkey PRIMARY KEY (id);


--
-- TOC entry 4973 (class 2606 OID 25900)
-- Name: monitoraggio monitoraggio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.monitoraggio
    ADD CONSTRAINT monitoraggio_pkey PRIMARY KEY (id);


--
-- TOC entry 4987 (class 2606 OID 25996)
-- Name: responsabile responsabile_codice_fiscale_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsabile
    ADD CONSTRAINT responsabile_codice_fiscale_key UNIQUE (codice_fiscale);


--
-- TOC entry 4989 (class 2606 OID 25998)
-- Name: responsabile responsabile_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsabile
    ADD CONSTRAINT responsabile_email_key UNIQUE (email);


--
-- TOC entry 4991 (class 2606 OID 26000)
-- Name: responsabile responsabile_id_utente_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsabile
    ADD CONSTRAINT responsabile_id_utente_key UNIQUE (id_utente);


--
-- TOC entry 4993 (class 2606 OID 25994)
-- Name: responsabile responsabile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsabile
    ADD CONSTRAINT responsabile_pkey PRIMARY KEY (id);


--
-- TOC entry 4975 (class 2606 OID 25902)
-- Name: ruolo ruolo_codice_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruolo
    ADD CONSTRAINT ruolo_codice_key UNIQUE (codice);


--
-- TOC entry 4977 (class 2606 OID 25904)
-- Name: ruolo ruolo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruolo
    ADD CONSTRAINT ruolo_pkey PRIMARY KEY (id_ruolo);


--
-- TOC entry 4979 (class 2606 OID 25906)
-- Name: tirocinio tirocinio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio
    ADD CONSTRAINT tirocinio_pkey PRIMARY KEY (id);


--
-- TOC entry 4981 (class 2606 OID 25908)
-- Name: utente utente_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);


--
-- TOC entry 4983 (class 2606 OID 25910)
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id_utente);


--
-- TOC entry 4985 (class 2606 OID 25912)
-- Name: utente utente_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_username_key UNIQUE (username);


--
-- TOC entry 4996 (class 2606 OID 25913)
-- Name: allievo allievo_corso_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.allievo
    ADD CONSTRAINT allievo_corso_id_fkey FOREIGN KEY (corso_id) REFERENCES public.corso(id);


--
-- TOC entry 4999 (class 2606 OID 25928)
-- Name: contatto_aziendale contatto_aziendale_azienda_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contatto_aziendale
    ADD CONSTRAINT contatto_aziendale_azienda_id_fkey FOREIGN KEY (azienda_id) REFERENCES public.azienda(id);


--
-- TOC entry 5000 (class 2606 OID 25933)
-- Name: documento_tirocinio documento_tirocinio_tirocinio_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_tirocinio
    ADD CONSTRAINT documento_tirocinio_tirocinio_id_fkey FOREIGN KEY (tirocinio_id) REFERENCES public.tirocinio(id);


--
-- TOC entry 4998 (class 2606 OID 25938)
-- Name: caso_critico fk_allievo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caso_critico
    ADD CONSTRAINT fk_allievo FOREIGN KEY (allievo_id) REFERENCES public.allievo(id) ON DELETE CASCADE;


--
-- TOC entry 4997 (class 2606 OID 26063)
-- Name: allievo fk_allievo_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.allievo
    ADD CONSTRAINT fk_allievo_utente FOREIGN KEY (id_utente) REFERENCES public.utente(id_utente) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 5006 (class 2606 OID 26031)
-- Name: colloquio_tirocinio fk_colloquio_allievo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colloquio_tirocinio
    ADD CONSTRAINT fk_colloquio_allievo FOREIGN KEY (allievo_id) REFERENCES public.allievo(id) ON DELETE CASCADE;


--
-- TOC entry 5007 (class 2606 OID 26036)
-- Name: colloquio_tirocinio fk_colloquio_azienda; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colloquio_tirocinio
    ADD CONSTRAINT fk_colloquio_azienda FOREIGN KEY (azienda_id) REFERENCES public.azienda(id) ON DELETE CASCADE;


--
-- TOC entry 5005 (class 2606 OID 26001)
-- Name: responsabile fk_responsabile_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsabile
    ADD CONSTRAINT fk_responsabile_utente FOREIGN KEY (id_utente) REFERENCES public.utente(id_utente) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 5004 (class 2606 OID 25943)
-- Name: utente fk_utente_ruolo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT fk_utente_ruolo FOREIGN KEY (id_ruolo) REFERENCES public.ruolo(id_ruolo) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 5001 (class 2606 OID 25948)
-- Name: monitoraggio monitoraggio_tirocinio_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.monitoraggio
    ADD CONSTRAINT monitoraggio_tirocinio_id_fkey FOREIGN KEY (tirocinio_id) REFERENCES public.tirocinio(id);


--
-- TOC entry 5002 (class 2606 OID 25953)
-- Name: tirocinio tirocinio_allievo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio
    ADD CONSTRAINT tirocinio_allievo_id_fkey FOREIGN KEY (allievo_id) REFERENCES public.allievo(id);


--
-- TOC entry 5003 (class 2606 OID 25958)
-- Name: tirocinio tirocinio_azienda_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio
    ADD CONSTRAINT tirocinio_azienda_id_fkey FOREIGN KEY (azienda_id) REFERENCES public.azienda(id);


-- Completed on 2026-03-28 22:22:37

--
-- PostgreSQL database dump complete
--

\unrestrict 8O9JcZEYbJGhbyd7RcqCeYh8jiQxpsnPT6bSUksha0f4pWlw0cecYzNOTKnhDra

