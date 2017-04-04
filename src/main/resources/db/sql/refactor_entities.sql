--
-- Schema refactor 
--

ALTER TABLE ONLY ride_provider 
    ADD COLUMN post_code character varying(255);

ALTER TABLE ONLY ride_order DROP CONSTRAINT IF EXISTS service_provider_id_fkey;

ALTER TABLE ONLY ride_order 
    ADD CONSTRAINT service_provider_id_fkey FOREIGN KEY (service_provider_id) REFERENCES ride_provider (id);
