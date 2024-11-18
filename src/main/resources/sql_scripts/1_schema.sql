-- Table: tb_orders
CREATE TABLE IF NOT EXISTS tb_orders (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    status INTEGER NOT NULL,
    is_anonymous BOOLEAN,
    time_to_prepare INTEGER NOT NULL,
    client_id UUID,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_update_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT tb_orders_pkey PRIMARY KEY (id)
);

-- Table: tb_order_items
CREATE TABLE IF NOT EXISTS tb_order_items (
    description VARCHAR(255),
    quantity INTEGER NOT NULL,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    CONSTRAINT tb_order_items_pkey PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id)
            REFERENCES tb_orders (id)
            ON UPDATE NO ACTION
            ON DELETE CASCADE
);
