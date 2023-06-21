CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

-- Password Admin@1234
INSERT INTO _user
(id, first_name, last_name, email, password, role, is_locked, created_at, created_by)
VALUES (uuid_generate_v4(), 'Admin', '', 'admin@gmail.com',
        '$2a$10$0zRLRtEGqUf/lom9mfTnueeJSkjognuMGEbmJOyf7TuJTkgszBqlW', 'ADMIN', false, now(), 'SYSTEM');

-- Password Sukses@2023
INSERT INTO _user
(id, first_name, last_name, email, password, role, is_locked, created_at, created_by)
VALUES (uuid_generate_v4(), 'Andree', 'Panjaitan', 'panjaitanandree@gmail.com',
        '$2a$10$TUxyLzZVvYAt4qLxEUZZjuCtJFUFhcwfdZ8fc3PB7sB7Ngu.xV3jm', 'PARKING_GUARD', false, now(),
        'admin@gmail.com');

INSERT INTO vehicle_type (id, name, created_at, created_by)
VALUES ('23bbeaee-c6c0-4685-9c6a-94c187871057', 'CAR', now(), 'SYSTEM'),
       ('36b22ca9-2f4c-44d5-8089-7aa4cd1a0da1', 'MOTORCYCLE', now(), 'SYSTEM'),
       ('e2c9e4c6-ad79-4e99-a3f1-ffae85026402', 'MINIBUS', now(), 'SYSTEM'),
       ('5539d21f-da4f-4c90-a974-8b3d2a01e5f2', 'BUS', now(), 'SYSTEM');

INSERT INTO location (id, address, city, code_area, name, created_at, created_by)
VALUES ('5775610f-506e-49bf-a848-0e73c60e5f18', 'Jln. Mekar', 'Medan Area', '22381', 'Parkir Mall', now(), 'SYSTEM');

INSERT INTO location_capacity (id, location_id, vehicle_type_id, capacity, price_per_hour, created_at, created_by)
VALUES (uuid_generate_v4(), '5775610f-506e-49bf-a848-0e73c60e5f18', '23bbeaee-c6c0-4685-9c6a-94c187871057', 100, 3000,
        now(), 'SYSTEM'),
       (uuid_generate_v4(), '5775610f-506e-49bf-a848-0e73c60e5f18', '36b22ca9-2f4c-44d5-8089-7aa4cd1a0da1', 100, 3000,
        now(), 'SYSTEM'),
       (uuid_generate_v4(), '5775610f-506e-49bf-a848-0e73c60e5f18', 'e2c9e4c6-ad79-4e99-a3f1-ffae85026402', 10, 3000,
        now(), 'SYSTEM'),
       (uuid_generate_v4(), '5775610f-506e-49bf-a848-0e73c60e5f18', '5539d21f-da4f-4c90-a974-8b3d2a01e5f2', 5, 3000,
        now(), 'SYSTEM');

INSERT INTO voucher (id,value, code_voucher, is_percentage, effective_date, expired_at, created_at, created_by)
    VALUES (uuid_generate_v4(),500, 'SUCCESS-TEST-2023', false, '2023-06-20', '2023-10-11',now(), 'SYSTEM');
