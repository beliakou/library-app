package com.smartexlab.libraryapp.repository.mapper;

import com.smartexlab.libraryapp.model.domain.BookDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookDtoMapper implements RowMapper<BookDto> {

    private static final String BOOK_ID = "book_id";
    private static final String BOOK_NAME = "book_name";
    private static final String AUTHOR_NAME = "author_name";

    @Override
    public BookDto mapRow(ResultSet resultSet, int i) throws SQLException {
        var bookDto = new BookDto();
        bookDto.setId(resultSet.getLong(BOOK_ID));
        bookDto.setBookName(resultSet.getString(BOOK_NAME));
        bookDto.setAuthorName(resultSet.getString(AUTHOR_NAME));
        return bookDto;
    }
}
