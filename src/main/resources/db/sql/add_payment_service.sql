--
-- Name: ride_order; Type: TABLE; Schema: store_user; Owner: store_user
--

CREATE TABLE ride_order (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    customer_id bigint NOT NULL,
    updated_at timestamp without time zone,
    payment_service_id character varying(255) NOT NULL
);


ALTER TABLE ride_order OWNER TO store_user;

--
-- Name: ride_order; Type: CONSTRAINT; Schema: store_user; Owner: store_user
--

ALTER TABLE ONLY ride_order
    ADD CONSTRAINT ride_order_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ride_order 
	ADD CONSTRAINT payment_service_id_unique UNIQUE (payment_service_id);

ALTER TABLE ONLY ride_order 
    ADD CONSTRAINT customer_id_fkey FOREIGN KEY (customer_id) REFERENCES customer (id);

--
-- Name: customer.payment_service_id; Schema: store_user; Owner: store_user
--

ALTER TABLE ONLY customer 
    ADD COLUMN payment_service_id character varying(255);

