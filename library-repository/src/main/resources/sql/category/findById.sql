SELECT  c.category_id as category_id,
        c.name as category_name
        FROM category c
        WHERE c.category_id = :categoryId;