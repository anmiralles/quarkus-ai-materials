-- ──────────────────────────────────────────────────────
-- DEV SEED DATA — loaded only in dev profile via
--   %dev.quarkus.flyway.locations=db/migration,db/testdata
-- ──────────────────────────────────────────────────────

-- ──────────────────────────────────────────────────────
-- Materials (10 rows — all MaterialCategory values)
-- ──────────────────────────────────────────────────────
INSERT INTO material (id, code, name, description, category, color, composition, supplier_ref, min_stock) VALUES
(1,  'FAB-001', 'Cotton Canvas 300gsm',      'Heavy-weight cotton canvas for structured garments',  'FABRIC',       'Natural',   '100% Cotton',            'SUP-FAB-01', 50.00),
(2,  'FAB-002', 'Linen Blend 200gsm',        'Lightweight linen blend for summer apparel',           'FABRIC',       'Ecru',      '55% Linen, 45% Cotton',  'SUP-FAB-02', 30.00),
(3,  'THR-001', 'Cotton Thread 40/2',        'General-purpose cotton sewing thread',                 'THREAD',       'White',     '100% Cotton',            'SUP-THR-01', 200.00),
(4,  'THR-002', 'Polyester Thread 50/3',     'High-tenacity polyester thread for seams',             'THREAD',       'Black',     '100% Polyester',         'SUP-THR-02', 150.00),
(5,  'BTN-001', 'Horn Button 15mm',          'Natural horn button, 4-hole, 15mm diameter',           'BUTTON',       'Brown',     'Natural Horn',           'SUP-BTN-01', 500.00),
(6,  'ZIP-001', 'YKK Metal Zipper 20cm',     'YKK brass metal zipper, closed-end, 20cm',             'ZIPPER',       'Brass',     'Brass/Polyester tape',   'SUP-ZIP-01', 100.00),
(7,  'LBL-001', 'Woven Brand Label',         'Main woven label with brand name and care symbols',    'LABEL',        NULL,        'Polyester',              'SUP-LBL-01', 1000.00),
(8,  'LIN-001', 'Cotton Lining 120gsm',      'Smooth cotton lining fabric',                          'LINING',       'White',     '100% Cotton',            'SUP-LIN-01', 40.00),
(9,  'INT-001', 'Fusible Interlining Medium','Medium-weight woven fusible interlining',               'INTERLINING',  NULL,        '100% Polyester',         'SUP-INT-01', 60.00),
(10, 'PKG-001', 'Kraft Paper Bag',           'Recycled kraft paper shopping bag with rope handles',  'PACKAGING',    'Brown',     'Recycled Kraft Paper',   'SUP-PKG-01', 200.00);

-- ──────────────────────────────────────────────────────
-- Products (6 rows — mix of statuses and categories)
-- ──────────────────────────────────────────────────────
INSERT INTO product (id, sku, name, description, category, status) VALUES
(1, 'SHIRT-001', 'Classic Oxford Shirt',  'Button-down Oxford shirt in 100% cotton canvas',   'SHIRT',     'ACTIVE'),
(2, 'PANT-001',  'Slim Fit Chinos',       'Slim-fit chinos in linen blend, zip fly',           'PANTS',     'ACTIVE'),
(3, 'JACK-001',  'Denim Jacket',          'Classic trucker jacket in heavy cotton canvas',     'JACKET',    'ACTIVE'),
(4, 'DRES-001',  'Summer Floral Dress',   'Lightweight A-line dress in linen blend',           'DRESS',     'DEVELOPMENT'),
(5, 'ACCS-001',  'Canvas Tote Bag',       'Durable everyday tote in cotton canvas',            'ACCESSORY', 'ACTIVE'),
(6, 'JACK-002',  'Vintage Blazer',        'Tailored blazer from a discontinued collection',    'JACKET',    'DISCONTINUED');

-- ──────────────────────────────────────────────────────
-- BOMs (5 rows — one per non-discontinued product)
-- ──────────────────────────────────────────────────────
INSERT INTO bom (id, product_id, version, status, effective_date) VALUES
(1, 1, 1, 'ACTIVE', '2025-01-15'),
(2, 2, 1, 'ACTIVE', '2025-02-01'),
(3, 3, 1, 'ACTIVE', '2025-03-01'),
(4, 4, 1, 'DRAFT',  NULL),
(5, 5, 1, 'ACTIVE', '2025-01-20');

-- ──────────────────────────────────────────────────────
-- BOM Items (14 rows — 2–4 items per BOM)
-- ──────────────────────────────────────────────────────

-- Oxford Shirt (bom_id=1): canvas, thread, button, label
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(1,  1, 1,  1.50, 5.00, '1.5m canvas per shirt, 5% cut waste'),
(2,  1, 3,  2.00, 2.00, '2 spools cotton thread'),
(3,  1, 5,  8.00, 0.00, '8 buttons per shirt'),
(4,  1, 7,  1.00, 0.00, 'Main brand label');

-- Slim Fit Chinos (bom_id=2): linen blend, thread, zipper
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(5,  2, 2,  1.80, 5.00, '1.8m linen blend per pair, 5% cut waste'),
(6,  2, 4,  1.50, 2.00, '1.5 spools polyester thread'),
(7,  2, 6,  1.00, 0.00, 'YKK zipper for fly');

-- Denim Jacket (bom_id=3): canvas, thread, zipper, interlining
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(8,  3, 1,  2.20, 8.00, '2.2m canvas per jacket, 8% cut waste'),
(9,  3, 3,  3.00, 2.00, '3 spools cotton thread'),
(10, 3, 6,  1.00, 0.00, 'YKK zipper for front'),
(11, 3, 9,  0.50, 3.00, '0.5m interlining for collar and cuffs');

-- Summer Floral Dress (bom_id=4): linen blend, thread, label
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(12, 4, 2,  2.00, 6.00, '2.0m linen blend per dress, 6% cut waste'),
(13, 4, 3,  1.50, 2.00, '1.5 spools cotton thread'),
(14, 4, 7,  1.00, 0.00, 'Main brand label');

-- Canvas Tote Bag (bom_id=5): canvas, thread, kraft bag
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(15, 5, 1,  0.60, 3.00, '0.6m canvas per tote, 3% cut waste'),
(16, 5, 3,  0.50, 2.00, '0.5 spools cotton thread'),
(17, 5, 10, 1.00, 0.00, 'Kraft bag for packaging');

-- ──────────────────────────────────────────────────────
-- Advance sequences past seed IDs to avoid collisions
-- with Hibernate 6 allocation size=50:
--   setval(seq, 100) → nextval returns 101
-- ──────────────────────────────────────────────────────
SELECT setval('material_seq', 100);
SELECT setval('product_seq',  100);
SELECT setval('bom_seq',      100);
SELECT setval('bomitem_seq',  100);
