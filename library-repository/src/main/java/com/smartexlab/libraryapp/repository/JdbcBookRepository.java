package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import com.smartexlab.libraryapp.model.domain.Category;
import com.smartexlab.libraryapp.model.exception.DataNotFoundException;
import com.smartexlab.libraryapp.model.exception.ServerSideException;
import com.smartexlab.libraryapp.repository.mapper.BookDtoMapper;
import com.smartexlab.libraryapp.repository.mapper.BookMapper;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
import com.smartexlab.libraryapp.repository.util.InjectSql;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcBookRepository implements BookRepository {

    @InjectSql("/sql/book_dto/findAll.sql")
    private String bookDtoFindAllSql;

    @InjectSql("/sql/book/findById.sql")
    private String findBookByIdSql;

    @InjectSql("/sql/category/findByBookId.sql")
    private String findBookCategoriesSql;

    private NamedParameterJdbcTemplate jdbcTemplate;
    private BookDtoMapper bookDtoMapper;
    private BookMapper bookMapper;
    private CategoryMapper categoryMapper;

    public JdbcBookRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            BookDtoMapper bookDtoMapper,
            BookMapper bookMapper,
            CategoryMapper categoryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookDtoMapper = bookDtoMapper;
        this.bookMapper = bookMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<BookDto> findAll() {
        return this.jdbcTemplate.query(this.bookDtoFindAllSql, this.bookDtoMapper);
    }

    @Override
    public Book findById(Long id) {
        try {
            Book book =
                    this.jdbcTemplate.queryForObject(
                            this.findBookByIdSql, Map.of("bookId", id), this.bookMapper);
            List<Category> categories =
                    this.jdbcTemplate.query(
                            this.findBookCategoriesSql, Map.of("bookId", id), this.categoryMapper);
            book.setCategories(new HashSet<>(categories));
            return book;
        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundException(String.format("Book with id=%s not found", id), ex);
        } catch (DataAccessException ex) {
            throw new com.smartexlab.libraryapp.model.exception.DataAccessException(
                    String.format("Cannot retireve " + "book with id=%s from database", id), ex);
        } catch (Exception ex) {
            throw new ServerSideException("Error occurred while accessing book data", ex);
        }
    }
}
