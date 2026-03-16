-- ──────────────────────────────────────────────────────
-- DEV SEED: material_attachment rows
-- Depends on: V1.0.3__Add_material_attachment.sql (table + seq)
--             V1.0.1__Dev_seed_data.sql (material rows 1-10)
-- ──────────────────────────────────────────────────────

INSERT INTO material_attachment
    (id, material_id, url, file_name, content_type, attachment_type, description, created_at)
VALUES

-- ── FAB-001  Cotton Canvas 300gsm (material_id=1) ───────────────────────────
(1,  1, 'https://cdn.example.com/materials/fab-001/swatch-natural.jpg',
        'swatch-natural.jpg',        'image/jpeg',        'IMAGE',    'Natural colour swatch photo',              '2025-01-10 09:00:00'),
(2,  1, 'https://cdn.example.com/materials/fab-001/spec-sheet.pdf',
        'FAB-001-spec-sheet.pdf',    'application/pdf',   'DOCUMENT', 'Technical specification sheet',            '2025-01-10 09:05:00'),
(3,  1, 'https://cdn.example.com/materials/fab-001/gots-certificate.pdf',
        'FAB-001-gots-cert.pdf',     'application/pdf',   'DOCUMENT', 'GOTS organic certification 2025',         '2025-01-12 14:30:00'),

-- ── FAB-002  Linen Blend 200gsm (material_id=2) ─────────────────────────────
(4,  2, 'https://cdn.example.com/materials/fab-002/swatch-ecru.jpg',
        'swatch-ecru.jpg',           'image/jpeg',        'IMAGE',    'Ecru colour swatch photo',                 '2025-02-03 10:00:00'),
(5,  2, 'https://cdn.example.com/materials/fab-002/swatch-ecru-closeup.jpg',
        'swatch-ecru-closeup.jpg',   'image/jpeg',        'IMAGE',    'Close-up weave detail',                    '2025-02-03 10:10:00'),
(6,  2, 'https://cdn.example.com/materials/fab-002/spec-sheet.pdf',
        'FAB-002-spec-sheet.pdf',    'application/pdf',   'DOCUMENT', 'Technical specification sheet',            '2025-02-03 10:15:00'),

-- ── THR-001  Cotton Thread 40/2 (material_id=3) ─────────────────────────────
(7,  3, 'https://cdn.example.com/materials/thr-001/spool-white.jpg',
        'spool-white.jpg',           'image/jpeg',        'IMAGE',    'White spool product photo',                '2025-01-15 08:00:00'),
(8,  3, 'https://cdn.example.com/materials/thr-001/datasheet.pdf',
        'THR-001-datasheet.pdf',     'application/pdf',   'DOCUMENT', 'Thread count and tensile strength data',   '2025-01-15 08:10:00'),

-- ── THR-002  Polyester Thread 50/3 (material_id=4) ──────────────────────────
(9,  4, 'https://cdn.example.com/materials/thr-002/spool-black.jpg',
        'spool-black.jpg',           'image/jpeg',        'IMAGE',    'Black spool product photo',                '2025-01-16 08:00:00'),
(10, 4, 'https://cdn.example.com/materials/thr-002/datasheet.pdf',
        'THR-002-datasheet.pdf',     'application/pdf',   'DOCUMENT', 'Thread count and tensile strength data',   '2025-01-16 08:10:00'),

-- ── BTN-001  Horn Button 15mm (material_id=5) ───────────────────────────────
(11, 5, 'https://cdn.example.com/materials/btn-001/button-front.jpg',
        'button-front.jpg',          'image/jpeg',        'IMAGE',    'Front face of horn button',                '2025-01-20 11:00:00'),
(12, 5, 'https://cdn.example.com/materials/btn-001/button-back.jpg',
        'button-back.jpg',           'image/jpeg',        'IMAGE',    'Back face showing 4-hole layout',          '2025-01-20 11:05:00'),
(13, 5, 'https://cdn.example.com/materials/btn-001/compliance-doc.pdf',
        'BTN-001-compliance.pdf',    'application/pdf',   'DOCUMENT', 'REACH compliance declaration',             '2025-01-20 11:15:00'),

-- ── ZIP-001  YKK Metal Zipper 20cm (material_id=6) ──────────────────────────
(14, 6, 'https://cdn.example.com/materials/zip-001/zipper-photo.jpg',
        'zipper-photo.jpg',          'image/jpeg',        'IMAGE',    'Product photo — brass finish',             '2025-02-05 09:00:00'),
(15, 6, 'https://cdn.example.com/materials/zip-001/ykk-spec.pdf',
        'ZIP-001-ykk-spec.pdf',      'application/pdf',   'DOCUMENT', 'YKK official product specification',       '2025-02-05 09:10:00'),

-- ── LBL-001  Woven Brand Label (material_id=7) ──────────────────────────────
(16, 7, 'https://cdn.example.com/materials/lbl-001/label-sample.jpg',
        'label-sample.jpg',          'image/jpeg',        'IMAGE',    'Woven label sample scan',                  '2025-01-08 15:00:00'),
(17, 7, 'https://cdn.example.com/materials/lbl-001/artwork-brief.pdf',
        'LBL-001-artwork-brief.pdf', 'application/pdf',   'DOCUMENT', 'Artwork brief and colour matching guide',  '2025-01-08 15:30:00'),

-- ── LIN-001  Cotton Lining 120gsm (material_id=8) ───────────────────────────
(18, 8, 'https://cdn.example.com/materials/lin-001/swatch-white.jpg',
        'swatch-white.jpg',          'image/jpeg',        'IMAGE',    'White lining swatch',                      '2025-02-10 09:00:00'),

-- ── INT-001  Fusible Interlining Medium (material_id=9) ─────────────────────
(19, 9, 'https://cdn.example.com/materials/int-001/interlining-sample.jpg',
        'interlining-sample.jpg',    'image/jpeg',        'IMAGE',    'Sample with fused face and back sides',    '2025-02-12 10:00:00'),
(20, 9, 'https://cdn.example.com/materials/int-001/fusibility-test.pdf',
        'INT-001-fusibility-test.pdf','application/pdf',  'DOCUMENT', 'Fusibility and wash-stability test report','2025-02-12 10:20:00');

-- ──────────────────────────────────────────────────────
-- Advance sequence past seed IDs.
-- material_attachment_seq INCREMENT=50; setval(100) → next alloc starts at 101.
-- ──────────────────────────────────────────────────────
SELECT setval('material_attachment_seq', 100);
