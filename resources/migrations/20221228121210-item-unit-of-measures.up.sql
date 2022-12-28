ALTER TABLE item
  ADD COLUMN supported_unit_of_measures unit_of_measure[] NOT NULL DEFAULT '{kg}';