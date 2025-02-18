INSERT INTO category (id, code, description) VALUES ('e4b8909c-e0e2-4192-a933-c75729cb5b0b', 1, 'grains and cereals') ON CONFLICT (id) DO NOTHING;
INSERT INTO category (id, code, description) VALUES ('7c9a5b3e-353d-40b3-ad17-287202880d12', 2, 'fruits') ON CONFLICT (id) DO NOTHING;
INSERT INTO category (id, code, description) VALUES ('4b2ad3b6-a8bd-44f0-a076-325c3af4bf1c', 3, 'vegetables') ON CONFLICT (id) DO NOTHING;

INSERT INTO supplier (id, name) VALUES ('cb59537e-cdd7-4b7e-965b-f93f45891f5a', 'Supplier 1') ON CONFLICT (id) DO NOTHING;
INSERT INTO supplier (id, name) VALUES ('71f5c7fa-3e7e-4535-af6e-1c0312ae98cb', 'Supplier 2') ON CONFLICT (id) DO NOTHING;

INSERT INTO uom (id, name) VALUES ('109764fa-7440-4103-94f2-40c2fd203ec3', 'package') ON CONFLICT (id) DO NOTHING;
INSERT INTO uom (id, name) VALUES ('217b7c68-4874-428a-afef-5ffe6cf9f271', 'pound') ON CONFLICT (id) DO NOTHING;

INSERT INTO product (id, name, quantity_available, fk_category, fk_supplier, fk_uom) VALUES ('3e3fd628-0bb0-438a-8cbb-f17d7c677f81', 'rice', 100, 'e4b8909c-e0e2-4192-a933-c75729cb5b0b', 'cb59537e-cdd7-4b7e-965b-f93f45891f5a', '109764fa-7440-4103-94f2-40c2fd203ec3') ON CONFLICT (id) DO NOTHING;
INSERT INTO product (id, name, quantity_available, fk_category, fk_supplier, fk_uom) VALUES ('1d88eab4-adbe-4617-80e5-693254d01a44', 'banana', 35.0, '7c9a5b3e-353d-40b3-ad17-287202880d12', '71f5c7fa-3e7e-4535-af6e-1c0312ae98cb', '217b7c68-4874-428a-afef-5ffe6cf9f271') ON CONFLICT (id) DO NOTHING;
INSERT INTO product (id, name, quantity_available, fk_category, fk_supplier, fk_uom) VALUES ('66d3ce77-1a4e-4454-b394-802b7b17390a', 'tomato', 45.0, '7c9a5b3e-353d-40b3-ad17-287202880d12', '71f5c7fa-3e7e-4535-af6e-1c0312ae98cb', '217b7c68-4874-428a-afef-5ffe6cf9f271') ON CONFLICT (id) DO NOTHING;
