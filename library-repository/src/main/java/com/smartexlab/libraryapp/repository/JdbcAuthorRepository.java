package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.api.respository.AuthorRepository;
import com.smartexlab.libraryapp.model.domain.Author;
import com.smartexlab.libraryapp.model.exception.DataNotFoundException;
import com.smartexlab.libraryapp.model.exception.ReadDataException;
import com.smartexlab.libraryapp.model.exception.ServerSideException;
import com.smartexlab.libraryapp.repository.mapper.AuthorMapper;
import com.smartexlab.libraryapp.repository.util.InjectSql;
import com.smartexlab.libraryapp.repository.util.ParamNames;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {
    @InjectSql("/sql/author/findById.sql")
    private String findAuthorByIdSql;

    private NamedParameterJdbcTemplate jdbcTemplate;
    private AuthorMapper AuthorMapper;

    public JdbcAuthorRepository(
            NamedParameterJdbcTemplate jdbcTemplate, AuthorMapper AuthorMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.AuthorMapper = AuthorMapper;
    }

    @Override
    public Author findById(Long id) {
        try {
            return this.jdbcTemplate.queryForObject(
                    this.findAuthorByIdSql, Map.of(ParamNames.AUTHOR_ID, id), this.AuthorMapper);
        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundException(String.format("Author with id=%s not found", id), ex);
        } catch (DataAccessException ex) {
            throw new ReadDataException(
                    String.format("Cannot retrieve author with id=%s from database", id), ex);
        } catch (Exception ex) {
            throw new ServerSideException("Error occurred while accessing author data", ex);
        }
    }
}
