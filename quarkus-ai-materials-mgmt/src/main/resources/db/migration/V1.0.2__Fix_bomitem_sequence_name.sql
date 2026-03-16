-- Rename bomitem_seq → bom_item_seq to match the table name (bom_item)
-- Hibernate 6 expects <table_name>_SEQ; the original migration missed the underscore.
ALTER SEQUENCE bomitem_seq RENAME TO bom_item_seq;
