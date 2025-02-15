CREATE TABLE category (
	id uuid NOT NULL,
	code varchar(255),
	description varchar(255),
	CONSTRAINT category_pkey PRIMARY KEY (id)
);

CREATE TABLE product (
    id uuid NOT NULL,
    "name" varchar(255),
	fk_category uuid NOT NULL,
	fk_supplier uuid NOT NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id)
);

CREATE TABLE supplier (
	id uuid NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT supplier_pkey PRIMARY KEY (id)
);