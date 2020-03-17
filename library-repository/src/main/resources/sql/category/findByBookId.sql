SELECT  c.category_id,
        c.name
    FROM category c
    JOIN
        (SELECT bc.book_id, bc.category_id FROM book_category bc
            WHERE bc.book_id = :bookId) AS target_c
    ON target_c.category_id = c.category_id;
