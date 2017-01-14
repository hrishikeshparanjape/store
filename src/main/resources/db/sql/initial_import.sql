--
-- Name: customer; Type: TABLE; Schema: store_user; Owner: store_user
--

CREATE TABLE customer (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    email character varying(255),
    updated_at timestamp without time zone
);


ALTER TABLE customer OWNER TO store_user;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: store_user; Owner: store_user
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO store_user;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: store_user; Owner: store_user
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: store_user; Owner: store_user
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);

