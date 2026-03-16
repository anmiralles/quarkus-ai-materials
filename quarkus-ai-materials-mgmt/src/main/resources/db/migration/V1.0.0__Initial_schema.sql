-- ──────────────────────────────────────────────────────
-- Sequences (Hibernate 6 uses per-entity sequences,
-- allocation size = 50 → INCREMENT BY 50)
-- ──────────────────────────────────────────────────────
CREATE SEQUENCE material_seq  START 1 INCREMENT 50;
CREATE SEQUENCE product_seq   START 1 INCREMENT 50;
CREATE SEQUENCE bom_seq       START 1 INCREMENT 50;
CREATE SEQUENCE bomitem_seq   START 1 INCREMENT 50;

-- ──────────────────────────────────────────────────────
-- material
-- ──────────────────────────────────────────────────────
CREATE TABLE material (
    id           BIGINT        NOT NULL,
    code         VARCHAR(255)  NOT NULL,
    name         VARCHAR(255)  NOT NULL,
    description  TEXT,
    category     VARCHAR(50)   NOT NULL,   -- MaterialCategory enum (STRING)
    color        VARCHAR(100),
    composition  VARCHAR(255),
    supplier_ref VARCHAR(255),
    min_stock    NUMERIC(12,2),
    CONSTRAINT pk_material      PRIMARY KEY (id),
    CONSTRAINT uq_material_code UNIQUE (code)
);

-- ──────────────────────────────────────────────────────
-- product
-- ──────────────────────────────────────────────────────
CREATE TABLE product (
    id          BIGINT        NOT NULL,
    sku         VARCHAR(255)  NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    description TEXT,
    category    VARCHAR(50)   NOT NULL,   -- ProductCategory enum (STRING)
    status      VARCHAR(50)   NOT NULL,   -- ProductStatus enum (STRING)
    CONSTRAINT pk_product     PRIMARY KEY (id),
    CONSTRAINT uq_product_sku UNIQUE (sku)
);

-- ──────────────────────────────────────────────────────
-- bom  (Bill of Materials — one per product/version pair)
-- ──────────────────────────────────────────────────────
CREATE TABLE bom (
    id             BIGINT       NOT NULL,
    product_id     BIGINT       NOT NULL,
    version        INTEGER      NOT NULL,
    status         VARCHAR(50)  NOT NULL,   -- BomStatus enum (STRING)
    effective_date DATE,
    CONSTRAINT pk_bom                 PRIMARY KEY (id),
    CONSTRAINT uq_bom_product_version UNIQUE (product_id, version),
    CONSTRAINT fk_bom_product         FOREIGN KEY (product_id) REFERENCES product (id)
);

-- ──────────────────────────────────────────────────────
-- bom_item  (line items within a BOM)
-- ──────────────────────────────────────────────────────
CREATE TABLE bom_item (
    id          BIGINT         NOT NULL,
    bom_id      BIGINT         NOT NULL,
    material_id BIGINT         NOT NULL,
    quantity    NUMERIC(12,4)  NOT NULL,
    waste_pct   NUMERIC(5,2),
    notes       TEXT,
    CONSTRAINT pk_bom_item      PRIMARY KEY (id),
    CONSTRAINT fk_bom_item_bom  FOREIGN KEY (bom_id)      REFERENCES bom (id),
    CONSTRAINT fk_bom_item_mat  FOREIGN KEY (material_id) REFERENCES material (id)
);
