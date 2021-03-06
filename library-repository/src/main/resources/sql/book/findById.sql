SELECT  b.book_id as book_id,
        b.isbn as book_isbn,
        b.name as book_name,
        b.update_time as book_update_time,
        a.author_id as author_id,
        a.name as author_name
    FROM book b
         JOIN author AS a ON a.author_id = b.author_id
    WHERE b.book_id = :bookId;
