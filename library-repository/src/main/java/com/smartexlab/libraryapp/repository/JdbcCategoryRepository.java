package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.api.respository.CategoryRepository;
import com.smartexlab.libraryapp.model.domain.Category;
import com.smartexlab.libraryapp.model.exception.DataNotFoundException;
import com.smartexlab.libraryapp.model.exception.ReadDataException;
import com.smartexlab.libraryapp.model.exception.ServerSideException;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
import com.smartexlab.libraryapp.repository.util.InjectSql;
import com.smartexlab.libraryapp.repository.util.ParamNames;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class JdbcCategoryRepository implements CategoryRepository {

    @InjectSql("/sql/category/findById.sql")
    private String findCategoryByIdSql;

    private NamedParameterJdbcTemplate jdbcTemplate;
    private CategoryMapper categoryMapper;

    public JdbcCategoryRepository(
            NamedParameterJdbcTemplate jdbcTemplate, CategoryMapper categoryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category findById(Long id) {
        try {
            return this.jdbcTemplate.queryForObject(
                    this.findCategoryByIdSql,
                    Map.of(ParamNames.CATEGORY_ID, id),
                    this.categoryMapper);
        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundException(String.format("Category with id=%s not found", id), ex);
        } catch (DataAccessException ex) {
            throw new ReadDataException(
                    String.format("Cannot retrieve category with id=%s from database", id), ex);
        } catch (Exception ex) {
            throw new ServerSideException("Error occurred while accessing category data", ex);
        }
    }
}
