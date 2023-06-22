-- public."_user" definition

-- Drop table

-- DROP TABLE public."_user";

CREATE TABLE public."_user" (
                                id uuid NOT NULL,
                                created_at timestamp(6) NULL,
                                created_by varchar(255) NULL,
                                deleted_at timestamp(6) NULL,
                                deleted_by varchar(255) NULL,
                                updated_at timestamp(6) NULL,
                                updated_by varchar(255) NULL,
                                email varchar(255) NOT NULL,
                                first_name varchar(255) NULL,
                                is_locked bool NOT NULL,
                                last_name varchar(255) NULL,
                                "password" varchar(255) NOT NULL,
                                "role" varchar(255) NULL,
                                CONSTRAINT "_user_pkey" PRIMARY KEY (id),
                                CONSTRAINT "_user_role_check" CHECK (((role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying, 'PARKING_GUARD'::character varying])::text[]))),
	CONSTRAINT uk_k11y3pdtsrjgy8w9b6q4bjwrx UNIQUE (email)
);


-- public."location" definition

-- Drop table

-- DROP TABLE public."location";

CREATE TABLE public."location" (
                                   id uuid NOT NULL,
                                   created_at timestamp(6) NULL,
                                   created_by varchar(255) NULL,
                                   deleted_at timestamp(6) NULL,
                                   deleted_by varchar(255) NULL,
                                   updated_at timestamp(6) NULL,
                                   updated_by varchar(255) NULL,
                                   address varchar(255) NULL,
                                   city varchar(255) NULL,
                                   code_area varchar(255) NULL,
                                   "name" varchar(255) NULL,
                                   CONSTRAINT location_pkey PRIMARY KEY (id),
                                   CONSTRAINT uk_fhgltxys5hcc89bhn1i9nvceb UNIQUE (code_area)
);


-- public.vehicle_type definition

-- Drop table

-- DROP TABLE public.vehicle_type;

CREATE TABLE public.vehicle_type (
                                     id uuid NOT NULL,
                                     created_at timestamp(6) NULL,
                                     created_by varchar(255) NULL,
                                     deleted_at timestamp(6) NULL,
                                     deleted_by varchar(255) NULL,
                                     updated_at timestamp(6) NULL,
                                     updated_by varchar(255) NULL,
                                     "name" varchar(255) NULL,
                                     CONSTRAINT vehicle_type_pkey PRIMARY KEY (id)
);


-- public.voucher definition

-- Drop table

-- DROP TABLE public.voucher;

CREATE TABLE public.voucher (
                                id uuid NOT NULL,
                                created_at timestamp(6) NULL,
                                created_by varchar(255) NULL,
                                deleted_at timestamp(6) NULL,
                                deleted_by varchar(255) NULL,
                                updated_at timestamp(6) NULL,
                                updated_by varchar(255) NULL,
                                code_voucher varchar(255) NULL,
                                effective_date timestamp(6) NULL,
                                expired_at timestamp(6) NULL,
                                is_percentage bool NOT NULL,
                                value numeric(38) NULL,
                                CONSTRAINT voucher_pkey PRIMARY KEY (id)
);


-- public.location_capacity definition

-- Drop table

-- DROP TABLE public.location_capacity;

CREATE TABLE public.location_capacity (
                                          id uuid NOT NULL,
                                          created_at timestamp(6) NULL,
                                          created_by varchar(255) NULL,
                                          deleted_at timestamp(6) NULL,
                                          deleted_by varchar(255) NULL,
                                          updated_at timestamp(6) NULL,
                                          updated_by varchar(255) NULL,
                                          capacity int4 NULL,
                                          price_per_hour numeric(38) NULL,
                                          location_id uuid NULL,
                                          vehicle_type_id uuid NULL,
                                          CONSTRAINT location_capacity_pkey PRIMARY KEY (id),
                                          CONSTRAINT fkjendfjev2mhhj1w3oaxe8xc6i FOREIGN KEY (vehicle_type_id) REFERENCES public.vehicle_type(id),
                                          CONSTRAINT fkjfodh8et0cl75b1kbht81mw16 FOREIGN KEY (location_id) REFERENCES public."location"(id)
);


-- public.location_guard definition

-- Drop table

-- DROP TABLE public.location_guard;

CREATE TABLE public.location_guard (
                                       id uuid NOT NULL,
                                       created_at timestamp(6) NULL,
                                       created_by varchar(255) NULL,
                                       deleted_at timestamp(6) NULL,
                                       deleted_by varchar(255) NULL,
                                       updated_at timestamp(6) NULL,
                                       updated_by varchar(255) NULL,
                                       entry_time timestamp(6) NULL,
                                       exit_time timestamp(6) NULL,
                                       guard_id uuid NULL,
                                       location_id uuid NULL,
                                       CONSTRAINT location_guard_pkey PRIMARY KEY (id),
                                       CONSTRAINT fkaqxo327ya6vb0feu1557uma5e FOREIGN KEY (guard_id) REFERENCES public."_user"(id),
                                       CONSTRAINT fkonshv11du4tys43eqyimrgr FOREIGN KEY (location_id) REFERENCES public."location"(id)
);


-- public."token" definition

-- Drop table

-- DROP TABLE public."token";

CREATE TABLE public."token" (
                                id uuid NOT NULL,
                                expired bool NOT NULL,
                                revoked bool NOT NULL,
                                "token" varchar(255) NULL,
                                token_type varchar(255) NULL,
                                user_id uuid NULL,
                                CONSTRAINT token_pkey PRIMARY KEY (id),
                                CONSTRAINT token_token_type_check CHECK (((token_type)::text = 'BEARER'::text)),
	CONSTRAINT uk_pddrhgwxnms2aceeku9s2ewy5 UNIQUE (token),
	CONSTRAINT fkiblu4cjwvyntq3ugo31klp1c6 FOREIGN KEY (user_id) REFERENCES public."_user"(id)
);


-- public.vehicle definition

-- Drop table

-- DROP TABLE public.vehicle;

CREATE TABLE public.vehicle (
                                plat_number varchar(255) NOT NULL,
                                created_at timestamp(6) NULL,
                                created_by varchar(255) NULL,
                                deleted_at timestamp(6) NULL,
                                deleted_by varchar(255) NULL,
                                updated_at timestamp(6) NULL,
                                updated_by varchar(255) NULL,
                                total_parking int8 NOT NULL,
                                vehicle_type_id uuid NULL,
                                CONSTRAINT vehicle_pkey PRIMARY KEY (plat_number),
                                CONSTRAINT fkddtxlc05rojc56bprvek17hnk FOREIGN KEY (vehicle_type_id) REFERENCES public.vehicle_type(id)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE public.orders (
                               id uuid NOT NULL,
                               created_at timestamp(6) NULL,
                               created_by varchar(255) NULL,
                               deleted_at timestamp(6) NULL,
                               deleted_by varchar(255) NULL,
                               updated_at timestamp(6) NULL,
                               updated_by varchar(255) NULL,
                               dif_string varchar(255) NULL,
                               entry_at timestamp(6) NULL,
                               entry_car_driver_image varchar(255) NULL,
                               entry_car_image varchar(255) NULL,
                               exit_at timestamp(6) NULL,
                               exit_car_driver_image varchar(255) NULL,
                               exit_car_image varchar(255) NULL,
                               is_paid bool NOT NULL,
                               price_per_hour numeric(38) NULL,
                               ticket_link varchar(255) NULL,
                               total_hour int4 NOT NULL,
                               total_price numeric(38) NULL,
                               unique_code varchar(255) NULL,
                               guard_id uuid NULL,
                               location_id uuid NULL,
                               vehicle_plat_number varchar(255) NULL,
                               CONSTRAINT orders_pkey PRIMARY KEY (id),
                               CONSTRAINT fkhiadc7rbqkne2164304so6edr FOREIGN KEY (vehicle_plat_number) REFERENCES public.vehicle(plat_number),
                               CONSTRAINT fkiwr50o78ka7370f239fyn49vs FOREIGN KEY (guard_id) REFERENCES public."_user"(id),
                               CONSTRAINT fkt6x8q6pvlj6oucw69uoicfaoj FOREIGN KEY (location_id) REFERENCES public."location"(id)
);


-- public.payment definition

-- Drop table

-- DROP TABLE public.payment;

CREATE TABLE public.payment (
                                id uuid NOT NULL,
                                created_at timestamp(6) NULL,
                                created_by varchar(255) NULL,
                                deleted_at timestamp(6) NULL,
                                deleted_by varchar(255) NULL,
                                updated_at timestamp(6) NULL,
                                updated_by varchar(255) NULL,
                                code_paid varchar(255) NULL,
                                original_price numeric(38) NULL,
                                paid_at timestamp(6) NULL,
                                payment_method varchar(255) NULL,
                                total_price numeric(38) NULL,
                                order_id uuid NULL,
                                CONSTRAINT payment_payment_method_check CHECK (((payment_method)::text = 'CASH'::text)),
	CONSTRAINT payment_pkey PRIMARY KEY (id),
	CONSTRAINT uk_mf7n8wo2rwrxsd6f3t9ub2mep UNIQUE (order_id),
	CONSTRAINT fklouu98csyullos9k25tbpk4va FOREIGN KEY (order_id) REFERENCES public.orders(id)
);


-- public.payment_voucher definition

-- Drop table

-- DROP TABLE public.payment_voucher;

CREATE TABLE public.payment_voucher (
                                        id uuid NOT NULL,
                                        created_at timestamp(6) NULL,
                                        created_by varchar(255) NULL,
                                        deleted_at timestamp(6) NULL,
                                        deleted_by varchar(255) NULL,
                                        updated_at timestamp(6) NULL,
                                        updated_by varchar(255) NULL,
                                        payment_id uuid NULL,
                                        voucher_id uuid NULL,
                                        CONSTRAINT payment_voucher_pkey PRIMARY KEY (id),
                                        CONSTRAINT fk9ycq9ksgd5a5eur9wipqdkb6q FOREIGN KEY (payment_id) REFERENCES public.payment(id),
                                        CONSTRAINT fkmw8qsejinx3pel10gcrs5i09e FOREIGN KEY (voucher_id) REFERENCES public.voucher(id)
);
