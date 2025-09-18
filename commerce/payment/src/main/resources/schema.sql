CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_cost DOUBLE PRECISION,
    delivery_cost DOUBLE PRECISION,
    total_cost DOUBLE PRECISION,
    state VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);