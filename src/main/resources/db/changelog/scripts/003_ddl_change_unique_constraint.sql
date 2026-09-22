ALTER TABLE warehouse_history
    ADD CONSTRAINT warehouse_history_without_duplicates
    UNIQUE(event, color, cotton_part, quantity, current_quantity, date);