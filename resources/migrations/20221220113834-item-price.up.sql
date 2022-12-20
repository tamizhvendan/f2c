CREATE TYPE "currency" AS ENUM ('INR'); 
--;;
CREATE TYPE "pricing_unit" AS ENUM ('piece', 'kg');
--;;
CREATE TABLE community.item_price (
  community_id UUID NOT NULL REFERENCES community(id)
, item_id UUID NOT NULL REFERENCES item(id)
, price DECIMAL NOT NULL CONSTRAINT positive_price CHECK (price > 0)
, pricing_unit pricing_unit NOT NULL DEFAULT 'kg'
, currency currency NOT NULL DEFAULT 'INR'
, valid_period TSTZRANGE NOT NULL DEFAULT TSTZRANGE(now() at TIME ZONE 'UTC', 'infinity')
);
--;;