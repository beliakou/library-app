SELECT b.book_id as book_id,
       b.name as book_name,
       a.name as author_name
    FROM book b
    JOIN author AS a ON a.author_id = b.book_id;
