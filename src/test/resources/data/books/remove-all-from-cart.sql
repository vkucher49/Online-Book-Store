SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM books;
DELETE FROM categories;
DELETE FROM books_categories;
DELETE FROM shopping_carts;
DELETE FROM cart_items;
DELETE FROM user;
SET FOREIGN_KEY_CHECKS = 1;