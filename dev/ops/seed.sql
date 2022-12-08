INSERT INTO "public"."community"("id","name")
VALUES
(E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'C1');

INSERT INTO "public"."individual"("id","name","mobile_number","password_hash")
VALUES
(E'0a51f1ac-f4e9-478d-8cd3-4b1c135a4470',E'John',E'7788997766',E'$2a$06$5uy.dGsQeRgG7j4tQCaVq.LvHlT58goqp2aJeVvKuMCaU03zxn3ry'),
(E'22f994e0-b89f-43b2-adc7-a13b0e6ef760',E'Sita',E'9988997766',E'$2a$06$Ss1a6i5/C2fCDfVdnOzMY.cisGyaGmWbxXixv6I7kPKbaXuk40.Ya'),
(E'8311d2d2-18e1-4001-b508-d4b588429716',E'Akbar',E'8888997766',E'$2a$06$Ab4lzGhQYBfC3C7AIuMgLuzdqojiJp/Fhw8gfPvJJdqVuhEl71i9u');

INSERT INTO "community"."individual"("id","community_id","individual_id","joined_on")
VALUES
(E'7c6458d4-671d-4290-a00c-890b43a00091',E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'0a51f1ac-f4e9-478d-8cd3-4b1c135a4470',E'2022-11-29'),
(E'41a26489-4cb0-4bff-80e8-f4b15d83b955',E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'22f994e0-b89f-43b2-adc7-a13b0e6ef760',E'2022-11-29'),
(E'a97e498b-c036-4e9a-b0ab-f8ded01e42af',E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'8311d2d2-18e1-4001-b508-d4b588429716',E'2022-11-29');

INSERT INTO "community"."facilitator"("id","community_id","individual_id")
VALUES
(E'608a7224-f2cb-4f4a-b149-61c4a0dceead',E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'0a51f1ac-f4e9-478d-8cd3-4b1c135a4470');


INSERT INTO "public"."item"("id","name")
VALUES
(E'dc1df659-4427-4d32-9747-3cf23de1596e',E'Big Onion'),
(E'd03cadcc-f5a9-4187-b1c4-4cf92e052d95',E'Garlic'),
(E'9a34bb9a-4a7a-4044-b846-20661d0cc619',E'Small Onion');

INSERT INTO "community"."item_availability"("community_id","item_id","is_available")
VALUES
(E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'9a34bb9a-4a7a-4044-b846-20661d0cc619',TRUE),
(E'74e06d97-cf9f-4133-b6e2-8f06c886f1cd',E'd03cadcc-f5a9-4187-b1c4-4cf92e052d95',TRUE);
