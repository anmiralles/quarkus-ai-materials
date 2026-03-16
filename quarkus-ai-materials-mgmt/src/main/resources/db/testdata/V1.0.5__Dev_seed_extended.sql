-- ──────────────────────────────────────────────────────
-- EXTENDED DEV SEED DATA — loaded only in dev profile via
--   %dev.quarkus.flyway.locations=db/migration,db/testdata
--
-- IDs 201–300 used for all tables (safe gap above two full
-- Hibernate allocation blocks: 101–200 already reserved).
-- ──────────────────────────────────────────────────────

-- ──────────────────────────────────────────────────────
-- Materials (20 rows — IDs 201–220, all 9 MaterialCategory values)
-- ──────────────────────────────────────────────────────
INSERT INTO material (id, code, name, description, category, color, composition, supplier_ref, min_stock) VALUES
(201, 'FAB-010', 'Merino Wool 180gsm',          'Fine merino wool for premium knitwear and tailoring',         'FABRIC',       'Ivory',         '100% Merino Wool',                  'SUP-FAB-10', 30.00),
(202, 'FAB-011', 'Silk Charmeuse 90gsm',         'Lightweight silk charmeuse for blouses and evening wear',     'FABRIC',       'Champagne',     '100% Silk',                         'SUP-FAB-11', 20.00),
(203, 'FAB-012', 'Stretch Denim 280gsm',         'Indigo stretch denim for fitted bottoms',                     'FABRIC',       'Indigo',        '98% Cotton, 2% Elastane',           'SUP-FAB-12', 40.00),
(204, 'FAB-013', 'Bamboo Jersey 160gsm',         'Sustainable bamboo jersey, soft and breathable',              'FABRIC',       'White',         '95% Bamboo, 5% Elastane',           'SUP-FAB-13', 35.00),
(205, 'THR-010', 'Silk Thread 60/2',             'Fine silk sewing thread for delicate fabrics',                'THREAD',       'Ivory',         '100% Silk',                         'SUP-THR-10', 300.00),
(206, 'THR-011', 'Nylon Thread 70/3',            'High-tenacity nylon thread for stretch seams',                'THREAD',       'Navy',          '100% Nylon',                        'SUP-THR-11', 250.00),
(207, 'BTN-010', 'Shell Button 12mm',            'Natural shell button, 4-hole, 12mm diameter',                 'BUTTON',       'White',         'Shell/Resin',                       'SUP-BTN-10', 800.00),
(208, 'BTN-011', 'Corozo Button 18mm',           'Natural corozo nut button, 2-hole, 18mm diameter',            'BUTTON',       'Tortoiseshell', 'Natural Corozo',                    'SUP-BTN-11', 600.00),
(209, 'ZIP-010', 'Nylon Coil Zipper 25cm',       'Lightweight nylon coil zipper, closed-end, 25cm',             'ZIPPER',       'Black',         'Polyester/Nylon',                   'SUP-ZIP-10', 150.00),
(210, 'ZIP-011', 'Metal Open-End Zipper 60cm',   'Heavy-duty open-end metal zipper for jackets, 60cm',          'ZIPPER',       'Gunmetal',      'Aluminium/Polyester tape',           'SUP-ZIP-11', 120.00),
(211, 'LBL-010', 'Satin Care Label',             'Printed satin label with care and size symbols',              'LABEL',        'White',         'Polyester Satin',                   'SUP-LBL-10', 2000.00),
(212, 'LBL-011', 'Woven Size Tab',               'Woven cotton twill size tab for garment interior',            'LABEL',        'White',         'Cotton Twill',                      'SUP-LBL-11', 2000.00),
(213, 'LIN-010', 'Silk Habotai Lining 60gsm',    'Lightweight silk habotai for luxury garment lining',          'LINING',       'Champagne',     '100% Silk',                         'SUP-LIN-10', 25.00),
(214, 'LIN-011', 'Acetate Lining 80gsm',         'Classic acetate lining fabric, smooth drape',                 'LINING',       'Black',         '100% Acetate',                      'SUP-LIN-11', 50.00),
(215, 'INT-010', 'Non-Woven Fusible 40gsm',      'Lightweight non-woven fusible interlining for shirt fronts',  'INTERLINING',  'Beige',         '100% Cotton',                       'SUP-INT-10', 80.00),
(216, 'ELS-001', 'Woven Elastic 25mm',           'Flat woven elastic band, 25mm wide, for waistbands',          'ELASTIC',      'Black',         '60% Polyester, 40% Rubber',         'SUP-ELS-01', 500.00),
(217, 'ELS-002', 'Soft Elastic 30mm',            'Plush soft elastic, 30mm wide, for intimate waistbands',      'ELASTIC',      'Nude',          '70% Polyester, 30% Rubber',         'SUP-ELS-02', 500.00),
(218, 'PKG-010', 'Recycled Paper Carrier Bag',   'Flat-bottom paper bag with twisted handle, FSC-certified',    'PACKAGING',    'White',         '100% Recycled Paper',               'SUP-PKG-10', 300.00),
(219, 'PKG-011', 'Bioplastic Poly Bag',          'Clear compostable garment bag, cornstarch-based',             'PACKAGING',    'Clear',         'Cornstarch Bioplastic',             'SUP-PKG-11', 400.00),
(220, 'PKG-012', 'Eco Gift Box with Ribbon',     'Rigid recycled card box with cotton string closure',          'PACKAGING',    'Natural',       'Recycled Card / Cotton String',     'SUP-PKG-12', 200.00);

-- ──────────────────────────────────────────────────────
-- Products (20 rows — IDs 201–220)
-- All 6 ProductCategory values and all 3 ProductStatus
-- values covered across the set.
-- ──────────────────────────────────────────────────────
INSERT INTO product (id, sku, name, description, category, status) VALUES
-- Shirts (201–203)
(201, 'SHIRT-002', 'Merino Knit Shirt',           'Fine merino wool shirt for smart-casual occasions',         'SHIRT',        'ACTIVE'),
(202, 'SHIRT-003', 'Silk Evening Blouse',          'Lightweight silk charmeuse blouse for formal wear',         'SHIRT',        'ACTIVE'),
(203, 'SHIRT-004', 'Stretch Denim Shirt',          'Slim-fit denim shirt in stretch indigo fabric',             'SHIRT',        'DEVELOPMENT'),
-- Pants (204–206)
(204, 'PANT-002',  'Canvas Workwear Trouser',      'Straight-leg cotton canvas trouser with elastic waist',     'PANTS',        'ACTIVE'),
(205, 'PANT-003',  'Bamboo Slim Pant',             'Slim-fit bamboo jersey pant with gunmetal zipper',          'PANTS',        'ACTIVE'),
(206, 'PANT-004',  'Stretch Skinny Jean',          'Ultra-slim stretch denim with nylon thread seams',          'PANTS',        'DEVELOPMENT'),
-- Jackets (207–210)
(207, 'JACK-003',  'Merino Tailored Jacket',       'Single-breasted tailored jacket in fine merino wool',       'JACKET',       'ACTIVE'),
(208, 'JACK-004',  'Summer Linen Jacket',          'Unstructured linen-blend jacket for warm weather',          'JACKET',       'ACTIVE'),
(209, 'JACK-005',  'Silk Evening Jacket',          'Luxurious silk jacket for evening occasions',               'JACKET',       'DEVELOPMENT'),
(210, 'JACK-006',  'Heritage Canvas Jacket',       'Classic canvas jacket from a discontinued archive line',    'JACKET',       'DISCONTINUED'),
-- Dresses (211–214)
(211, 'DRES-002',  'Merino Wrap Dress',            'Wrap-style merino wool dress with silk lining',             'DRESS',        'ACTIVE'),
(212, 'DRES-003',  'Silk Occasion Dress',          'Floor-length silk charmeuse evening dress',                 'DRESS',        'ACTIVE'),
(213, 'DRES-004',  'Bamboo Jersey Dress',          'Casual jersey dress in sustainable bamboo fabric',          'DRESS',        'DEVELOPMENT'),
(214, 'DRES-005',  'Linen Midi Dress',             'A-line midi dress from a discontinued season',              'DRESS',        'DISCONTINUED'),
-- Accessories (215–217)
(215, 'ACCS-002',  'Canvas Belt Bag',              'Compact canvas belt bag with elastic closure',              'ACCESSORY',    'ACTIVE'),
(216, 'ACCS-003',  'Stretch Coin Pouch',           'Small stretch-fabric pouch with recycled card backing',     'ACCESSORY',    'ACTIVE'),
(217, 'ACCS-004',  'Bamboo Wristlet',              'Sustainable bamboo jersey wristlet with elastic loop',      'ACCESSORY',    'DEVELOPMENT'),
-- Home textiles (218–220)
(218, 'HOME-001',  'Merino Throw Blanket',         'Luxurious merino wool throw with silk-backed lining',       'HOME_TEXTILE', 'ACTIVE'),
(219, 'HOME-002',  'Linen Table Runner',           'Softened linen-blend table runner with acetate backing',    'HOME_TEXTILE', 'ACTIVE'),
(220, 'HOME-003',  'Bamboo Cushion Cover',         'Breathable bamboo jersey cushion cover in development',     'HOME_TEXTILE', 'DEVELOPMENT');

-- ──────────────────────────────────────────────────────
-- BOMs (20 rows — IDs 201–220, one per product)
-- ACTIVE BOMs carry an effective_date; DRAFT BOMs do not.
-- OBSOLETE BOMs correspond to discontinued products.
-- ──────────────────────────────────────────────────────
INSERT INTO bom (id, product_id, version, status, effective_date) VALUES
(201, 201, 1, 'ACTIVE',   '2025-06-01'),
(202, 202, 1, 'ACTIVE',   '2025-07-01'),
(203, 203, 1, 'DRAFT',    NULL),
(204, 204, 1, 'ACTIVE',   '2025-06-15'),
(205, 205, 1, 'ACTIVE',   '2025-08-01'),
(206, 206, 1, 'DRAFT',    NULL),
(207, 207, 1, 'ACTIVE',   '2025-05-01'),
(208, 208, 1, 'ACTIVE',   '2025-09-01'),
(209, 209, 1, 'DRAFT',    NULL),
(210, 210, 1, 'OBSOLETE', '2024-01-01'),
(211, 211, 1, 'ACTIVE',   '2025-06-01'),
(212, 212, 1, 'ACTIVE',   '2025-10-01'),
(213, 213, 1, 'DRAFT',    NULL),
(214, 214, 1, 'OBSOLETE', '2023-06-01'),
(215, 215, 1, 'ACTIVE',   '2025-04-01'),
(216, 216, 1, 'ACTIVE',   '2025-11-01'),
(217, 217, 1, 'DRAFT',    NULL),
(218, 218, 1, 'ACTIVE',   '2025-05-15'),
(219, 219, 1, 'ACTIVE',   '2025-12-01'),
(220, 220, 1, 'DRAFT',    NULL);

-- ──────────────────────────────────────────────────────
-- BOM Items (100 rows — IDs 201–300, 5 per BOM)
-- Each BOM draws from both existing materials (IDs 1–10)
-- and new materials (IDs 201–220) to exercise cross-seed FK.
-- ──────────────────────────────────────────────────────

-- BOM 201 — Merino Knit Shirt (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(201, 201, 201, 1.8000,  5.00, '1.8m merino wool per shirt, 5% cut waste'),
(202, 201, 205, 2.0000,  2.00, '2 spools silk thread for fine seams'),
(203, 201, 207, 8.0000,  0.00, '8 shell buttons per shirt'),
(204, 201, 211, 1.0000,  0.00, 'Satin care label inside collar'),
(205, 201, 215, 0.4000,  3.00, '0.4m interlining for front placket');

-- BOM 202 — Silk Evening Blouse (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(206, 202, 202, 1.6000,  6.00, '1.6m silk charmeuse per blouse, 6% cut waste'),
(207, 202, 205, 1.5000,  2.00, '1.5 spools silk thread'),
(208, 202, 208, 10.0000, 0.00, '10 corozo buttons per blouse'),
(209, 202, 212, 1.0000,  0.00, 'Woven size tab'),
(210, 202, 218, 1.0000,  0.00, 'Recycled paper carrier bag for packaging');

-- BOM 203 — Stretch Denim Shirt (DRAFT)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(211, 203, 203, 1.7000,  5.00, '1.7m stretch denim per shirt, 5% cut waste'),
(212, 203, 206, 2.0000,  2.00, 'Navy nylon thread for denim seams'),
(213, 203, 207, 9.0000,  0.00, '9 shell buttons per shirt'),
(214, 203, 211, 1.0000,  0.00, 'Satin care label'),
(215, 203, 215, 0.3500,  3.00, '0.35m interlining for collar band');

-- BOM 204 — Canvas Workwear Trouser (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(216, 204, 1,   2.0000,  6.00, '2.0m cotton canvas per pair, 6% cut waste'),
(217, 204, 4,   2.0000,  2.00, '2 spools polyester thread'),
(218, 204, 209, 1.0000,  0.00, 'Black nylon coil zipper for fly'),
(219, 204, 216, 0.8000,  2.00, 'Woven elastic waistband, 25mm'),
(220, 204, 211, 1.0000,  0.00, 'Satin care label');

-- BOM 205 — Bamboo Slim Pant (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(221, 205, 204, 2.1000,  6.00, '2.1m bamboo jersey per pair, 6% cut waste'),
(222, 205, 206, 2.0000,  2.00, 'Nylon thread for stretch seams'),
(223, 205, 210, 1.0000,  0.00, 'Gunmetal open-end zipper for fly'),
(224, 205, 217, 0.8000,  2.00, 'Soft nude elastic waistband, 30mm'),
(225, 205, 212, 1.0000,  0.00, 'Woven size tab');

-- BOM 206 — Stretch Skinny Jean (DRAFT)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(226, 206, 203, 2.0000,  5.00, '2.0m stretch denim per pair, 5% cut waste'),
(227, 206, 4,   1.8000,  2.00, '1.8 spools polyester thread'),
(228, 206, 209, 1.0000,  0.00, 'Black nylon zipper for fly'),
(229, 206, 216, 0.7000,  2.00, 'Woven elastic waistband, 25mm'),
(230, 206, 211, 1.0000,  0.00, 'Satin care label');

-- BOM 207 — Merino Tailored Jacket (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(231, 207, 201, 2.5000,  8.00, '2.5m merino wool per jacket, 8% cut waste'),
(232, 207, 205, 3.0000,  2.00, '3 spools silk thread'),
(233, 207, 209, 1.0000,  0.00, 'Black nylon zipper for front'),
(234, 207, 213, 2.0000,  5.00, 'Full silk habotai lining'),
(235, 207, 215, 0.6000,  3.00, '0.6m interlining for body and lapels');

-- BOM 208 — Summer Linen Jacket (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(236, 208, 2,   2.3000,  8.00, '2.3m linen blend per jacket, 8% cut waste'),
(237, 208, 3,   3.0000,  2.00, '3 spools cotton thread'),
(238, 208, 210, 1.0000,  0.00, 'Gunmetal open-end zipper for front'),
(239, 208, 214, 1.8000,  5.00, 'Acetate lining for smooth finish'),
(240, 208, 212, 1.0000,  0.00, 'Woven size tab inside collar');

-- BOM 209 — Silk Evening Jacket (DRAFT)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(241, 209, 202, 2.0000,  8.00, '2.0m silk charmeuse per jacket, 8% cut waste'),
(242, 209, 205, 2.5000,  2.00, 'Silk thread for delicate seams'),
(243, 209, 209, 1.0000,  0.00, 'Black nylon zipper for front'),
(244, 209, 213, 1.5000,  5.00, 'Silk habotai inner lining'),
(245, 209, 215, 0.4000,  3.00, '0.4m interlining for lapels');

-- BOM 210 — Heritage Canvas Jacket (OBSOLETE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(246, 210, 1,   2.8000, 10.00, '2.8m cotton canvas per jacket, 10% cut waste'),
(247, 210, 4,   3.5000,  2.00, '3.5 spools polyester thread'),
(248, 210, 6,   1.0000,  0.00, 'YKK brass zipper for front (original spec)'),
(249, 210, 214, 2.2000,  6.00, 'Acetate lining'),
(250, 210, 7,   1.0000,  0.00, 'Woven brand label (original spec)');

-- BOM 211 — Merino Wrap Dress (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(251, 211, 201, 2.5000,  6.00, '2.5m merino wool per dress, 6% cut waste'),
(252, 211, 205, 2.0000,  2.00, 'Silk thread for fine seams'),
(253, 211, 211, 1.0000,  0.00, 'Satin care label'),
(254, 211, 213, 2.0000,  5.00, 'Full silk habotai lining'),
(255, 211, 218, 1.0000,  0.00, 'Recycled paper carrier bag');

-- BOM 212 — Silk Occasion Dress (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(256, 212, 202, 3.0000,  7.00, '3.0m silk charmeuse per dress, 7% cut waste'),
(257, 212, 205, 2.5000,  2.00, 'Silk thread for delicate seams'),
(258, 212, 212, 1.0000,  0.00, 'Woven size tab'),
(259, 212, 213, 2.5000,  5.00, 'Full silk habotai lining'),
(260, 212, 219, 1.0000,  0.00, 'Compostable bioplastic garment bag');

-- BOM 213 — Bamboo Jersey Dress (DRAFT)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(261, 213, 204, 2.2000,  6.00, '2.2m bamboo jersey per dress, 6% cut waste'),
(262, 213, 206, 2.0000,  2.00, 'Nylon thread for stretch seams'),
(263, 213, 211, 1.0000,  0.00, 'Satin care label'),
(264, 213, 214, 1.8000,  5.00, 'Acetate lining'),
(265, 213, 218, 1.0000,  0.00, 'Recycled paper carrier bag');

-- BOM 214 — Linen Midi Dress (OBSOLETE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(266, 214, 2,   2.8000,  7.00, '2.8m linen blend per dress, 7% cut waste'),
(267, 214, 3,   2.5000,  2.00, 'Cotton thread'),
(268, 214, 7,   1.0000,  0.00, 'Woven brand label (original spec)'),
(269, 214, 8,   2.2000,  5.00, 'Cotton lining fabric'),
(270, 214, 10,  1.0000,  0.00, 'Kraft paper bag (original spec)');

-- BOM 215 — Canvas Belt Bag (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(271, 215, 1,   0.8000,  3.00, '0.8m cotton canvas per belt bag, 3% cut waste'),
(272, 215, 3,   0.5000,  2.00, '0.5 spools cotton thread'),
(273, 215, 211, 1.0000,  0.00, 'Satin care label'),
(274, 215, 218, 1.0000,  0.00, 'Recycled paper packaging bag'),
(275, 215, 216, 0.3000,  2.00, 'Elastic closure band, 25mm');

-- BOM 216 — Stretch Coin Pouch (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(276, 216, 203, 0.5000,  3.00, '0.5m stretch denim per pouch, 3% cut waste'),
(277, 216, 206, 0.4000,  2.00, 'Nylon thread for stretch seams'),
(278, 216, 212, 1.0000,  0.00, 'Woven size tab'),
(279, 216, 220, 1.0000,  0.00, 'Eco gift box for retail packaging'),
(280, 216, 217, 0.4000,  2.00, 'Soft nude elastic closure, 30mm');

-- BOM 217 — Bamboo Wristlet (DRAFT)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(281, 217, 204, 0.6000,  3.00, '0.6m bamboo jersey per wristlet, 3% cut waste'),
(282, 217, 205, 0.3000,  2.00, 'Silk thread for fine seams'),
(283, 217, 211, 1.0000,  0.00, 'Satin care label'),
(284, 217, 219, 1.0000,  0.00, 'Compostable bioplastic garment bag'),
(285, 217, 216, 0.3000,  2.00, 'Elastic loop closure, 25mm');

-- BOM 218 — Merino Throw Blanket (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(286, 218, 201, 3.0000,  5.00, '3.0m merino wool per blanket, 5% cut waste'),
(287, 218, 3,   2.0000,  2.00, 'Cotton thread for blanket-stitch hem'),
(288, 218, 211, 1.0000,  0.00, 'Satin care label'),
(289, 218, 218, 1.0000,  0.00, 'Recycled paper carrier bag'),
(290, 218, 213, 2.5000,  4.00, 'Silk habotai inner lining layer');

-- BOM 219 — Linen Table Runner (ACTIVE)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(291, 219, 2,   3.5000,  5.00, '3.5m linen blend per runner, 5% cut waste'),
(292, 219, 4,   2.0000,  2.00, 'Polyester thread for hemming'),
(293, 219, 212, 1.0000,  0.00, 'Woven size tab with care symbols'),
(294, 219, 220, 1.0000,  0.00, 'Eco gift box for retail packaging'),
(295, 219, 214, 3.0000,  4.00, 'Acetate backing layer for stability');

-- BOM 220 — Bamboo Cushion Cover (DRAFT)
INSERT INTO bom_item (id, bom_id, material_id, quantity, waste_pct, notes) VALUES
(296, 220, 204, 4.0000,  5.00, '4.0m bamboo jersey per cover, 5% cut waste'),
(297, 220, 205, 2.5000,  2.00, 'Silk thread for fine edge-stitching'),
(298, 220, 211, 1.0000,  0.00, 'Satin care label'),
(299, 220, 218, 1.0000,  0.00, 'Recycled paper carrier bag'),
(300, 220, 213, 3.5000,  4.00, 'Silk habotai inner lining layer');

-- ──────────────────────────────────────────────────────
-- Advance sequences past all seed IDs (201–300) to avoid
-- collisions with Hibernate 6 allocation size=50:
--   setval(seq, 300) → next nextval returns 301
-- ──────────────────────────────────────────────────────
SELECT setval('material_seq', 300);
SELECT setval('product_seq',  300);
SELECT setval('bom_seq',      300);
SELECT setval('bom_item_seq', 300);
