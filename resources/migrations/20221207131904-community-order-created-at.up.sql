ALTER TABLE community."order"
  ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT (now() at TIME ZONE 'UTC');