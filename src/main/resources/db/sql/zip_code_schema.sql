--
-- Name: zip_code; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE zip_code (
    id bigint NOT NULL,
    code character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE zip_code OWNER TO storeuser;

--
-- Name: zip_code_id_seq; Type: SEQUENCE; Schema: storeuser; Owner: storeuser
--

CREATE SEQUENCE zip_code_id_seq;

ALTER SEQUENCE zip_code_id_seq OWNER TO storeuser;

ALTER TABLE zip_code ALTER COLUMN id SET DEFAULT nextval('zip_code_id_seq');

--
-- Name: zip_code_neighbors; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE zip_code_neighbors (
    zip_code_id bigint NOT NULL,
    neighbors_id bigint NOT NULL
);


ALTER TABLE zip_code_neighbors OWNER TO storeuser;

--
-- Name: zip_code_neighbors zip_code_neighbors_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY zip_code_neighbors
    ADD CONSTRAINT zip_code_neighbors_pkey PRIMARY KEY (zip_code_id, neighbors_id);

--
-- Name: zip_code zip_code_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY zip_code
    ADD CONSTRAINT zip_code_pkey PRIMARY KEY (id);

--
-- Name: zip_code_neighbors fkf4h9k6so5mtne42e1v3nfqj8e; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY zip_code_neighbors
    ADD CONSTRAINT fkf4h9k6so5mtne42e1v3nfqj8e FOREIGN KEY (neighbors_id) REFERENCES zip_code(id);

--
-- Name: zip_code_neighbors fkpn4oc4l3cuwivo940dux3f24a; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY zip_code_neighbors
    ADD CONSTRAINT fkpn4oc4l3cuwivo940dux3f24a FOREIGN KEY (zip_code_id) REFERENCES zip_code(id);
