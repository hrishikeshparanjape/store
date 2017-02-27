--
-- Name: ride_provider; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE ride_provider (
    id bigint NOT NULL,
    capacity numeric(19,2),
    city character varying(255),
    created_at timestamp without time zone,
    geo_location character varying(255),
    is_online boolean NOT NULL,
    rides_in_progress numeric(19,2),
    updated_at timestamp without time zone,
    customer_id bigint
);


ALTER TABLE ride_provider OWNER TO storeuser;

--
-- Name: ride_provider ride_provider_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY ride_provider
    ADD CONSTRAINT ride_provider_pkey PRIMARY KEY (id);

--
-- Name: ride_provider fknn0l3n06dqp810un8clwbetgc; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY ride_provider
    ADD CONSTRAINT fknn0l3n06dqp810un8clwbetgc FOREIGN KEY (customer_id) REFERENCES customer(id);
