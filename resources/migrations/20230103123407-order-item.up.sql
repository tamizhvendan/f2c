CREATE TABLE individual.order_item (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, individual_order_id UUID NOT NULL REFERENCES individual."order"(id)
, item_id UUID NOT NULL REFERENCES item(id)
, quantity DECIMAL NOT NULL
, unit unit_of_measure NOT NULL
, UNIQUE (individual_order_id, item_id)
);
--;;