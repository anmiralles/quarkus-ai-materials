CREATE SEQUENCE material_attachment_seq START 1 INCREMENT 50;

CREATE TABLE material_attachment (
    id              BIGINT        NOT NULL,
    material_id     BIGINT        NOT NULL,
    url             VARCHAR(2048) NOT NULL,
    file_name       VARCHAR(512),
    content_type    VARCHAR(255),
    attachment_type VARCHAR(50)   NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP     NOT NULL,
    CONSTRAINT pk_material_attachment     PRIMARY KEY (id),
    CONSTRAINT fk_mat_attachment_material FOREIGN KEY (material_id)
        REFERENCES material (id) ON DELETE CASCADE
);

CREATE INDEX idx_mat_attachment_material ON material_attachment (material_id);
CREATE INDEX idx_mat_attachment_type     ON material_attachment (material_id, attachment_type);
