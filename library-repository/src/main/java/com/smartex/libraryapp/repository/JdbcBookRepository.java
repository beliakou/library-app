package com.smartex.libraryapp.repository;

import com.smartex.libraryapp.api.service.BookRepository;
import com.smartex.libraryapp.model.Author;
import com.smartex.libraryapp.model.Book;
import com.smartex.libraryapp.model.Category;
import com.smartex.libraryapp.repository.mapper.BookMapper;
import com.smartex.libraryapp.repository.mapper.CategoryMapper;
import com.smartex.libraryapp.repository.sql.InjectSql;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class JdbcBookRepository implements BookRepository {

    @InjectSql("/sql/book/findBooks.sql")
    private String findBooksSql;

    @InjectSql("/sql/book/findBookCategories.sql")
    private String findBookCategoriesSql;

    private NamedParameterJdbcTemplate jdbcTemplate;
    private BookMapper bookMapper;
    private CategoryMapper categoryMapper;

    public JdbcBookRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            BookMapper bookMapper,
            CategoryMapper categoryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookMapper = bookMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Book findBookById(Long id) {
        return null;
    }

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        return null;
    }

    @Override
    public List<Book> findBooks() {
        return this.jdbcTemplate.query(this.findBooksSql, this.bookMapper).stream()
                .peek(book -> book.getCategories().addAll(this.findBookCategories(book.getId())))
                .collect(Collectors.toList());
    }

    private List<Category> findBookCategories(Long bookId) {
        return this.jdbcTemplate.query(
                this.findBookCategoriesSql,
                Map.<String, Object>of("bookId", bookId),
                this.categoryMapper);
    }
}
