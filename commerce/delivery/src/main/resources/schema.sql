CREATE TABLE IF NOT EXISTS deliveries (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    total_volume DOUBLE PRECISION,
    total_weight DOUBLE PRECISION,
    is_fragile BOOLEAN,
    delivery_address VARCHAR(500),
    warehouse_address VARCHAR(500),
    state VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);