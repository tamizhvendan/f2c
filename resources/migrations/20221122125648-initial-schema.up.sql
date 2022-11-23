CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--;;
CREATE TABLE "community" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, name VARCHAR(256) NOT NULL
);
CREATE TABLE "individual" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, name VARCHAR(256) NOT NULL
, mobile_number VARCHAR(10) NOT NULL UNIQUE
, password_hash TEXT NOT NULL
);
--;;
CREATE SCHEMA "community";
--;;
CREATE TABLE "community"."individual" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, community_id UUID NOT NULL REFERENCES community(id)
, individual_id UUID NOT NULL REFERENCES individual(id)
, joined_on DATE NOT NULL CURRENT_DATE
, UNIQUE (community_id, individual_id)
);
--;;
CREATE TABLE "community"."facilitator" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
, community_id UUID NOT NULL REFERENCES community(id)
, individual_id UUID NOT NULL REFERENCES individual(id)
);
--;; 
CREATE OR REPLACE FUNCTION check_facilitator_is_a_part_of_community() 
   RETURNS TRIGGER AS $$ 
DECLARE
    is_valid_facilitator BOOLEAN;
BEGIN
   SELECT true 
   INTO is_valid_facilitator
   FROM "community"."individual"
   WHERE community_id = NEW.community_id AND individual_id = NEW.individual_id;
   ASSERT is_valid_facilitator, 'Only individuals from the community are allowed to become the facilitator of the community';
   RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;
--;; 
CREATE OR REPLACE TRIGGER check_facilitator_is_a_part_of_community_trig
  BEFORE INSERT OR UPDATE ON "community"."facilitator"
  FOR EACH ROW EXECUTE PROCEDURE check_facilitator_is_a_part_of_community();
--;;