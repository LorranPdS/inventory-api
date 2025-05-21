CREATE TABLE uom (
	id uuid NOT NULL,
	"name" varchar(40) NOT NULL,
	CONSTRAINT uom_pkey PRIMARY KEY (id)
);

ALTER TABLE product ADD COLUMN quantity_available double precision NOT NULL DEFAULT 0 CHECK (quantity_available >= 0);
ALTER TABLE product ADD COLUMN fk_uom uuid NOT NULL;
ALTER TABLE product ADD CONSTRAINT fk_uom FOREIGN KEY (fk_uom) REFERENCES uom(id);
