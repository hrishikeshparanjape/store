--
-- Name: address; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE address (
    id bigint NOT NULL,
    city character varying(255),
    country character varying(255),
    created_at timestamp without time zone,
    line1 character varying(255),
    line2 character varying(255),
    line3 character varying(255),
    line4 character varying(255),
    locality character varying(255),
    post_code character varying(255),
    state character varying(255),
    updated_at timestamp without time zone
);

ALTER TABLE address OWNER TO storeuser;

--
-- Name: address address_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);

--
-- New columns 
--

ALTER TABLE ONLY customer 
    ADD COLUMN roles character varying(255);

ALTER TABLE ONLY ride_order 
    ADD COLUMN end_location_id bigint;

ALTER TABLE ONLY ride_order 
    ADD COLUMN start_location_id bigint;

ALTER TABLE ONLY ride_order 
    ADD COLUMN service_provider_id bigint;

--
-- New constraints 
--
    
ALTER TABLE ONLY ride_order 
    ADD CONSTRAINT end_location_id_fkey FOREIGN KEY (end_location_id) REFERENCES address (id);

ALTER TABLE ONLY ride_order 
    ADD CONSTRAINT start_location_id_fkey FOREIGN KEY (start_location_id) REFERENCES address (id);
    
ALTER TABLE ONLY ride_order 
    ADD CONSTRAINT service_provider_id_fkey FOREIGN KEY (service_provider_id) REFERENCES customer (id);

    