package com.smartex.libraryapp.repository.mapper;

import com.smartex.libraryapp.model.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryMapper implements RowMapper<Category> {

    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME = "category_name";

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        var category = new Category();

        category.setId(resultSet.getLong(CATEGORY_ID));
        category.setName(resultSet.getString(CATEGORY_NAME));

        return category;
    }
}
