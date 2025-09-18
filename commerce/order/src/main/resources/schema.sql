CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    shopping_cart_id UUID NOT NULL,
    delivery_id UUID,
    payment_id UUID,
    state VARCHAR(50) NOT NULL,
    total_volume DOUBLE PRECISION,
    total_weight DOUBLE PRECISION,
    is_fragile BOOLEAN,
    total_price DOUBLE PRECISION,
    products_price DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);