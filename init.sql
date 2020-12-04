DROP TABLE IF EXISTS public.account;
DROP TABLE IF EXISTS public.currency_account;
DROP TABLE IF EXISTS public.card;
DROP TABLE IF EXISTS public.currency;
DROP TABLE IF EXISTS public."user";

CREATE TABLE public."user"
(
    id uuid NOT NULL,
    id_number text COLLATE pg_catalog."default" NOT NULL,
    phone text COLLATE pg_catalog."default",
    password text COLLATE pg_catalog."default",
    token text COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE public.card
(
    id uuid NOT NULL,
    "number" text COLLATE pg_catalog."default",
    type text COLLATE pg_catalog."default",
    CONSTRAINT card_pkey PRIMARY KEY (id)
);

CREATE TABLE public.account
(
    id uuid NOT NULL,
    user_id uuid,
    card_id uuid,
    CONSTRAINT account_pkey PRIMARY KEY (id),
    CONSTRAINT account_card_id_fkey FOREIGN KEY (card_id)
        REFERENCES public.card (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT account_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE public.currency
(
    id uuid NOT NULL,
    name text COLLATE pg_catalog."default",
    CONSTRAINT currency_pkey PRIMARY KEY (id)
);
insert into public.currency values('d555f6d4-d75b-4925-8df7-094946950ff4', 'USD');
insert into public.currency values('dc78530d-ed9b-42f8-bc2c-37cbf60e7e78', 'AMD');
insert into public.currency values('372f37de-bb03-4c3b-b86e-52d7c0091af0', 'EUR');

CREATE TABLE public.currency_account
(
    id uuid NOT NULL,
    card_id uuid,
    currency_id uuid,
    amount numeric,
    CONSTRAINT currency_account_pkey PRIMARY KEY (id),
    CONSTRAINT currency_account_card_id_fkey FOREIGN KEY (card_id)
        REFERENCES public.card (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT currency_account_currency_id_fkey FOREIGN KEY (currency_id)
        REFERENCES public.currency (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);