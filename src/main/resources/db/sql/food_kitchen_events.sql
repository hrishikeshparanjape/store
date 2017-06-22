--
-- Name: food_category; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE food_category (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    name character varying(255),
    updated_at timestamp without time zone
);


ALTER TABLE food_category OWNER TO storeuser;

--
-- Name: food_event; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE food_event (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    ends_at timestamp without time zone,
    starts_at timestamp without time zone,
    updated_at timestamp without time zone,
    kitchen_id bigint
);


ALTER TABLE food_event OWNER TO storeuser;

--
-- Name: food_event_menu_items; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE food_event_menu_items (
    food_event_id bigint NOT NULL,
    menu_items_id bigint NOT NULL
);


ALTER TABLE food_event_menu_items OWNER TO storeuser;

--
-- Name: food_item; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE food_item (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    name character varying(255),
    updated_at timestamp without time zone
);


ALTER TABLE food_item OWNER TO storeuser;

--
-- Name: food_item_categories; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE food_item_categories (
    food_item_id bigint NOT NULL,
    categories_id bigint NOT NULL
);


ALTER TABLE food_item_categories OWNER TO storeuser;

--
-- Name: kitchen; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE kitchen (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    address_id bigint,
    owner_id bigint
);


ALTER TABLE kitchen OWNER TO storeuser;

--
-- Name: kitchen_events; Type: TABLE; Schema: storeuser; Owner: storeuser
--

CREATE TABLE kitchen_events (
    kitchen_id bigint NOT NULL,
    events_id bigint NOT NULL
);


ALTER TABLE kitchen_events OWNER TO storeuser;

--
-- Name: food_category food_category_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_category
    ADD CONSTRAINT food_category_pkey PRIMARY KEY (id);


--
-- Name: food_event_menu_items food_event_menu_items_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_event_menu_items
    ADD CONSTRAINT food_event_menu_items_pkey PRIMARY KEY (food_event_id, menu_items_id);


--
-- Name: food_event food_event_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_event
    ADD CONSTRAINT food_event_pkey PRIMARY KEY (id);


--
-- Name: food_item_categories food_item_categories_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_item_categories
    ADD CONSTRAINT food_item_categories_pkey PRIMARY KEY (food_item_id, categories_id);


--
-- Name: food_item food_item_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_item
    ADD CONSTRAINT food_item_pkey PRIMARY KEY (id);


--
-- Name: kitchen_events kitchen_events_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY kitchen_events
    ADD CONSTRAINT kitchen_events_pkey PRIMARY KEY (kitchen_id, events_id);


--
-- Name: kitchen kitchen_pkey; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY kitchen
    ADD CONSTRAINT kitchen_pkey PRIMARY KEY (id);

--
-- Name: kitchen_events uk_1s2pnqcd04wj1oifvft4ryjvl; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--


ALTER TABLE ONLY kitchen_events
    ADD CONSTRAINT uk_1s2pnqcd04wj1oifvft4ryjvl UNIQUE (events_id);


--
-- Name: food_event_menu_items uk_hgivwvrmnk6fb48r3d5au2hkr; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_event_menu_items
    ADD CONSTRAINT uk_hgivwvrmnk6fb48r3d5au2hkr UNIQUE (menu_items_id);


--
-- Name: food_item_categories uk_ns6h3jrii1f58qcb094a5pg6e; Type: CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_item_categories
    ADD CONSTRAINT uk_ns6h3jrii1f58qcb094a5pg6e UNIQUE (categories_id);


--
-- Name: food_event_menu_items fk2a0k26chnagov2cy45ihs4q6m; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_event_menu_items
    ADD CONSTRAINT fk2a0k26chnagov2cy45ihs4q6m FOREIGN KEY (menu_items_id) REFERENCES food_item(id);


--
-- Name: kitchen fk3g6uekao0dp4s3vwphwui812a; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY kitchen
    ADD CONSTRAINT fk3g6uekao0dp4s3vwphwui812a FOREIGN KEY (owner_id) REFERENCES customer(id);


--
-- Name: food_event_menu_items fk9vm079okf92yt5m61673juhij; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_event_menu_items
    ADD CONSTRAINT fk9vm079okf92yt5m61673juhij FOREIGN KEY (food_event_id) REFERENCES food_event(id);


--
-- Name: food_event fkd2u4jbm1715ixatu117k5hrjd; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_event
    ADD CONSTRAINT fkd2u4jbm1715ixatu117k5hrjd FOREIGN KEY (kitchen_id) REFERENCES kitchen(id);


--
-- Name: kitchen_events fkjt81qnycu9295ofme94qhr2hx; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY kitchen_events
    ADD CONSTRAINT fkjt81qnycu9295ofme94qhr2hx FOREIGN KEY (kitchen_id) REFERENCES kitchen(id);


--
-- Name: kitchen fkk5g64a6gu9nom0kr6qsi3skkh; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY kitchen
    ADD CONSTRAINT fkk5g64a6gu9nom0kr6qsi3skkh FOREIGN KEY (address_id) REFERENCES address(id);


--
-- Name: food_item_categories fkncx3byqdms6kf2pxcboahqx7m; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_item_categories
    ADD CONSTRAINT fkncx3byqdms6kf2pxcboahqx7m FOREIGN KEY (food_item_id) REFERENCES food_item(id);


--
-- Name: kitchen_events fknh1k7g32esfax9nqhrtuoft1b; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY kitchen_events
    ADD CONSTRAINT fknh1k7g32esfax9nqhrtuoft1b FOREIGN KEY (events_id) REFERENCES food_event(id);


--
-- Name: food_item_categories fkqnox82hn6ag19q8967l1ax0am; Type: FK CONSTRAINT; Schema: storeuser; Owner: storeuser
--

ALTER TABLE ONLY food_item_categories
    ADD CONSTRAINT fkqnox82hn6ag19q8967l1ax0am FOREIGN KEY (categories_id) REFERENCES food_category(id);

