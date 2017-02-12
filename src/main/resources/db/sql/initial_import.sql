--
-- Name: customer; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE customer (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    email character varying(255) NOT NULL,
    updated_at timestamp without time zone
);


ALTER TABLE customer OWNER TO storeuser;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: storeuser; Owner: storeuser
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO storeuser;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: storeuser; Owner: storeuser
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);

ALTER TABLE ONLY customer 
	ADD CONSTRAINT customer_email_unique UNIQUE (email);
