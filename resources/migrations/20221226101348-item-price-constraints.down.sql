ALTER TABLE community.item_price
  DROP CONSTRAINT no_valid_period_overlap,
  DROP CONSTRAINT valid_transction_period;
--;;
ALTER TYPE "unit_of_measure" RENAME TO "pricing_unit";
--;;