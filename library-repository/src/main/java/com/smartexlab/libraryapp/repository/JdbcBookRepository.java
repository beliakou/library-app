package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import com.smartexlab.libraryapp.model.domain.Category;
import com.smartexlab.libraryapp.model.exception.DataNotFoundException;
import com.smartexlab.libraryapp.model.exception.ReadDataException;
import com.smartexlab.libraryapp.model.exception.ServerSideException;
import com.smartexlab.libraryapp.model.exception.UpdateDataException;
import com.smartexlab.libraryapp.repository.mapper.BookDtoMapper;
import com.smartexlab.libraryapp.repository.mapper.BookMapper;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
import com.smartexlab.libraryapp.repository.util.InjectSql;
import com.smartexlab.libraryapp.repository.util.ParamNames;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
public class JdbcBookRepository implements BookRepository {

    @InjectSql("/sql/book_dto/findAll.sql")
    private String bookDtoFindAllSql;

    @InjectSql("/sql/book/findById.sql")
    private String findBookByIdSql;

    @InjectSql("/sql/category/findByBookId.sql")
    private String findBookCategoriesSql;

    @InjectSql("/sql/book/createBook.sql")
    private String createBookSql;

    @InjectSql("/sql/book/updateBook.sql")
    private String updateBookSql;

    @InjectSql("/sql/book/deleteBookCategoryByBookId.sql")
    private String deleteBookCategoryByBookIdSql;

    @InjectSql("/sql/book/createBookCategory.sql")
    private String createBookCategorySql;

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
        try {
            return this.jdbcTemplate.query(this.bookDtoFindAllSql, this.bookDtoMapper);
        } catch (DataAccessException ex) {
            throw new ReadDataException("Cannot retrieve books from database", ex);
        } catch (Exception ex) {
            throw new ServerSideException("Error occurred while accessing book data", ex);
        }
    }

    @Override
    public Book findById(Long id) {
        try {
            Book book =
                    this.jdbcTemplate.queryForObject(
                            this.findBookByIdSql, Map.of(ParamNames.BOOK_ID, id), this.bookMapper);
            List<Category> categories =
                    this.jdbcTemplate.query(
                            this.findBookCategoriesSql,
                            Map.of(ParamNames.BOOK_ID, id),
                            this.categoryMapper);
            book.setCategories(new HashSet<>(categories));
            return book;
        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundException(String.format("Book with id=%s not found", id), ex);
        } catch (Exception ex) {
            throw new ReadDataException(
                    String.format("Cannot retrieve book with id=%s from database", id), ex);
        }
    }

    @Override
    public Long createBook(Book book) {
        try {
            Long createdBookId =
                    createBook(book.getIsbn(), book.getName(), book.getAuthor().getId());
            addBookCategories(book.getCategories(), createdBookId);
            return createdBookId;
        } catch (Exception ex) {
            throw new UpdateDataException("Cannot create book", ex);
        }
    }

    @Override
    public void updateBook(Book book) {
        try {
            deleteBookCategories(book);
            addBookCategories(book.getCategories(), book.getId());
            updateBook(book.getId(), book.getIsbn(), book.getName(), book.getAuthor().getId());
        } catch (Exception ex) {
            throw new UpdateDataException(
                    String.format("Cannot update book with id=%s", book.getId()), ex);
        }
    }

    private void addBookCategories(Set<Category> categories, Long bookId) {
        categories.stream()
                .map(Category::getId)
                .map(
                        categoryId ->
                                CompletableFuture.runAsync(
                                        () ->
                                                this.jdbcTemplate.update(
                                                        createBookCategorySql,
                                                        Map.of(
                                                                ParamNames.BOOK_ID,
                                                                bookId,
                                                                ParamNames.CATEGORY_ID,
                                                                categoryId))))
                .collect(Collectors.toList())
                .forEach(CompletableFuture::join);
    }

    private void deleteBookCategories(Book book) {
        this.jdbcTemplate.update(
                deleteBookCategoryByBookIdSql, Map.of(ParamNames.BOOK_ID, book.getId()));
    }

    private void updateBook(Long bookId, String isbn, String name, Long authorId) {
        MapSqlParameterSource params =
                new MapSqlParameterSource(
                        Map.of(
                                ParamNames.BOOK_ID,
                                bookId,
                                ParamNames.BOOK_ISBN,
                                isbn,
                                ParamNames.BOOK_NAME,
                                name,
                                ParamNames.AUTHOR_ID,
                                authorId));

        this.jdbcTemplate.update(updateBookSql, params);
    }

    private Long createBook(String isbn, String name, Long authorId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params =
                new MapSqlParameterSource(
                        Map.of(
                                ParamNames.BOOK_ISBN,
                                isbn,
                                ParamNames.BOOK_NAME,
                                name,
                                ParamNames.AUTHOR_ID,
                                authorId));
        String[] keys = {"book_id"};

        this.jdbcTemplate.update(createBookSql, params, keyHolder, keys);

        return keyHolder.getKey().longValue();
    }
}
