
INSERT INTO author (author_id, name) VALUES (1, 'Charles Dickens');
INSERT INTO author (author_id, name) VALUES (2, 'Isaac Asimov');
INSERT INTO author (author_id, name) VALUES (3, 'John Keats');

INSERT INTO category (category_id, name) VALUES (1, 'classic');
INSERT INTO category (category_id, name) VALUES (2, 'modern');
INSERT INTO category (category_id, name) VALUES (3, 'fiction');
INSERT INTO category (category_id, name) VALUES (4, 'novel');
INSERT INTO category (category_id, name) VALUES (5, 'science');
INSERT INTO category (category_id, name) VALUES (6, 'poetry');

INSERT INTO book (isbn, name, author_id) VALUES ('1536870439', 'The Pickwick Papers', 1);
INSERT INTO book (isbn, name, author_id) VALUES ('978-0008117498', 'Foundation', 2);
INSERT INTO book (isbn, name, author_id) VALUES ('978-0375756696', 'Complete Poems and Selected Letters of John Keats ', 3);

INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
INSERT INTO book_category (book_id, category_id) VALUES (1, 4);
INSERT INTO book_category (book_id, category_id) VALUES (2, 2);
INSERT INTO book_category (book_id, category_id) VALUES (2, 3);
INSERT INTO book_category (book_id, category_id) VALUES (2, 5);
INSERT INTO book_category (book_id, category_id) VALUES (3, 6);
