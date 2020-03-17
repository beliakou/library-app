package com.smartexlab.libraryapp.repository.mapper;

import com.smartexlab.libraryapp.model.Author;
import com.smartexlab.libraryapp.model.Book;
import com.smartexlab.libraryapp.model.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashSet;

@Component
public class BookMapper implements RowMapper<Book> {

    private static final String BOOK_ID = "book_id";
    private static final String BOOK_ISBN = "book_isbn";
    private static final String BOOK_NAME = "book_name";
    private static final String BOOK_UPDATE_TIME = "book_update_time";
    private static final String AUTHOR_ID = "author_id";
    private static final String AUTHOR_NAME = "author_name";

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {

        var book = new Book();
        var author = new Author();

        book.setId(resultSet.getLong(BOOK_ID));
        book.setIsbn(resultSet.getString(BOOK_ISBN));
        book.setName(resultSet.getString(BOOK_NAME));
        book.setUpdateTime(resultSet.getObject(BOOK_UPDATE_TIME, OffsetDateTime.class));

        author.setId(resultSet.getLong(AUTHOR_ID));
        author.setName(resultSet.getString(AUTHOR_NAME));

        book.setAuthor(author);
        book.setCategories(new HashSet<Category>());

        return book;
    }
}
