UPDATE book
    SET isbn = :isbn,
        name = :name,
        author_id = :authorId
    WHERE book_id = :bookId;
