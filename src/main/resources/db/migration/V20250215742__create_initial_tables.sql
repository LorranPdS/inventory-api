CREATE TABLE category (
	id uuid NOT NULL,
	code varchar(255),
	description varchar(255),
	CONSTRAINT category_pkey PRIMARY KEY (id)
);

CREATE TABLE product (
    id uuid NOT NULL,
    "name" varchar(255),
	category_id uuid NOT NULL,
	supplier_id uuid NOT NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id)
);

CREATE TABLE supplier (
	id uuid NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT supplier_pkey PRIMARY KEY (id)
);