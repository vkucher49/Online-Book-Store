INSERT INTO books (id, title, author, isbn, price, cover_image, description)
VALUES (1 ,'Book 1', 'Author 1', '12345', 100, 'image1.jpg', 'Description book 1');
INSERT INTO books (id, title, author, isbn, price, cover_image, description)
VALUES (2, 'Book 2', 'Author 2', '56789', 200, 'image2.jpg', 'Description book 2');
INSERT INTO categories (id, name, description) VALUES (1, 'Category A', 'Description category A');
INSERT INTO categories (id, name, description) VALUES (2, 'Category B', 'Description category B');
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 2);
INSERT INTO user (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'user@gmail.com', 'abc123', 'firstName', 'lastName', 'address');
INSERT INTO shopping_carts (user_id)
VALUES (1);
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 10);
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (2, 1, 2, 20);