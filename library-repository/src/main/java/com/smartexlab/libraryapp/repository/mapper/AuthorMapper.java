package com.smartexlab.libraryapp.repository.mapper;

import com.smartexlab.libraryapp.model.domain.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorMapper implements RowMapper<Author> {

    private static final String AUTHOR_ID = "author_id";
    private static final String AUTHOR_NAME = "author_name";

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        var author = new Author();
        author.setId(rs.getLong(AUTHOR_ID));
        author.setName(rs.getString(AUTHOR_NAME));
        return author;
    }

}
