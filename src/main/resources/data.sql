INSERT INTO product (name, description, material, price, stock)
SELECT 'Vase Model A', 'Elegant 3D printed vase', 'PLA', 29.99, 50
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Vase Model A');

INSERT INTO product (name, description, material, price, stock)
SELECT 'Phone Stand', 'Adjustable desk phone stand', 'PETG', 14.99, 100
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Phone Stand');

INSERT INTO product (name, description, material, price, stock)
SELECT 'Cable Clip Set', 'Set of 6 cable management clips', 'TPU', 9.99, 200
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Cable Clip Set');