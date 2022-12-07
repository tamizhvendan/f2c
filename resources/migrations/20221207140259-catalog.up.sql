CREATE EXTENSION IF NOT EXISTS citext;
--;;
CREATE TABLE item (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, name CITEXT NOT NULL UNIQUE
);
--;;
CREATE TABLE community.item_availability (
  community_id UUID NOT NULL REFERENCES community(id)
, item_id UUID NOT NULL REFERENCES item(id)
, is_available BOOLEAN NOT NULL DEFAULT TRUE
);
--;;