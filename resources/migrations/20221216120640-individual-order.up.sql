CREATE SCHEMA "individual";
--;;
CREATE TABLE "individual"."order" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, individual_id UUID NOT NULL REFERENCES "individual"(id)
, community_order_id UUID NOT NULL REFERENCES "community"."order"(id)
, UNIQUE (individual_id,community_order_id)
);
--;;