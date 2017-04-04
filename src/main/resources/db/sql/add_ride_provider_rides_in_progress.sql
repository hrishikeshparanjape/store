ALTER TABLE ride_provider DROP COLUMN rides_in_progress;

--
-- Name: ride_provider_rides_in_progress; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE ride_provider_rides_in_progress (
    ride_provider_id bigint NOT NULL,
    rides_in_progress_id bigint NOT NULL
);


ALTER TABLE ride_provider_rides_in_progress OWNER TO storeuser;

--
-- Name: ride_provider_rides_in_progress uk_seu78ogga1h8fahwl58s956r0; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY ride_provider_rides_in_progress
    ADD CONSTRAINT uk_seu78ogga1h8fahwl58s956r0 UNIQUE (rides_in_progress_id);

--
-- Name: ride_provider_rides_in_progress fkg1gyk4v0cvca5v3gkw76dvg60; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY ride_provider_rides_in_progress
    ADD CONSTRAINT fkg1gyk4v0cvca5v3gkw76dvg60 FOREIGN KEY (rides_in_progress_id) REFERENCES ride_order(id);

--
-- Name: ride_provider_rides_in_progress fkq2glp8vipp1h0q8pnjs1xydyg; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY ride_provider_rides_in_progress
    ADD CONSTRAINT fkq2glp8vipp1h0q8pnjs1xydyg FOREIGN KEY (ride_provider_id) REFERENCES ride_provider(id);



