ALTER TABLE warehouse_history ADD COLUMN IF NOT EXISTS
    current_quantity INT NOT NULL CHECK (current_quantity >= 0);