CREATE EXTENSION IF NOT EXISTS btree_gist;
--;;
-- https://www.postgresql.org/docs/15/rangetypes.html#RANGETYPES-CONSTRAINT
ALTER TABLE community.item_price
  
  ADD CONSTRAINT no_valid_period_overlap EXCLUDE USING GIST 
    (community_id with =, item_id with =, pricing_unit with =, currency with =, valid_period with &&),

  ADD CONSTRAINT valid_transction_period 
    CHECK (
      (lower(valid_period) < now() at TIME ZONE 'UTC' AND upper(valid_period) = now() at TIME ZONE 'UTC')
      or 
      (lower(valid_period) = now() at TIME ZONE 'UTC' AND upper(valid_period) = 'infinity')
    ) NOT VALID; 
--;;
ALTER TYPE "pricing_unit" RENAME TO "unit_of_measure";
--;;
