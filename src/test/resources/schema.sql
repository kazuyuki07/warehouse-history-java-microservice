CREATE TABLE IF NOT EXISTS warehouse_history (
    id SERIAL PRIMARY KEY,
    event VARCHAR(20) NOT NULL CHECK (event IN ('INCOME', 'OUTCOME')),
    color VARCHAR(50) NOT NULL,
    cotton_part INT NOT NULL CHECK (cotton_part BETWEEN 0 AND 100),
    quantity INT NOT NULL CHECK (quantity >= 0),
    warehouse_id BIGINT NOT NULL CHECK (warehouse_id >= 0),
    date TIMESTAMP NOT NULL,
    UNIQUE(event, color, cotton_part, quantity, warehouse_id, date)
);