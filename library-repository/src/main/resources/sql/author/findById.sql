SELECT  a.author_id as author_id,
        a.name as author_name
        FROM author a
        WHERE a.author_id = :authorId;