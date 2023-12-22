DELETE FROM orders WHERE id BETWEEN 1 and 3;
DELETE FROM order_items WHERE id BETWEEN 1 and 3;
DELETE FROM cart_items WHERE id BETWEEN 1 and 3;
DELETE FROM shopping_carts WHERE user_id BETWEEN 1 and 3;
DELETE FROM books_categories WHERE category_id BETWEEN 1 and 3 AND book_id BETWEEN 1 and 3;
DELETE FROM categories WHERE id BETWEEN 1 and 3;
DELETE FROM users_roles WHERE user_id BETWEEN 1 and 3;
DELETE FROM user WHERE id BETWEEN 1 and 3;
DELETE FROM books WHERE id BETWEEN 1 and 3;