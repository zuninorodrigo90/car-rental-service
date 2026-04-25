INSERT INTO car (id, patent, brand, model, price, type, available)
VALUES
(random_uuid(), 'AAA111', 'BMW', '7', 300, 'PREMIUM', true),
(random_uuid(), 'AAA112', 'Audi', 'A8', 300, 'PREMIUM', true),

(random_uuid(), 'BBB111', 'Kia', 'Sorento', 150, 'SUV', true),
(random_uuid(), 'BBB112', 'Nissan', 'Juke', 150, 'SUV', true),

(random_uuid(), 'CCC111', 'Seat', 'Ibiza', 50, 'SMALL', true),
(random_uuid(), 'CCC112', 'Fiat', '500', 50, 'SMALL', true);


INSERT INTO customer (id, dni, name, email, loyalty_points)
VALUES
(random_uuid(), '30111222', 'Rodrigo', 'rodrigo@mail.com', 0),
(random_uuid(), '28999111', 'Manuel', 'manuel@mail.com', 0);