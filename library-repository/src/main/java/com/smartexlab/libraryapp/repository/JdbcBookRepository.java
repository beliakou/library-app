package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.Book;
import com.smartexlab.libraryapp.model.BookDto;
import com.smartexlab.libraryapp.model.Category;
import com.smartexlab.libraryapp.repository.mapper.BookDtoMapper;
import com.smartexlab.libraryapp.repository.mapper.BookMapper;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
import com.smartexlab.libraryapp.repository.util.InjectSql;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        Book book =
                this.jdbcTemplate.queryForObject(
                        this.findBookByIdSql, Map.of("bookId", id), this.bookMapper);
        if (book != null) {
            List<Category> categories =
                    this.jdbcTemplate.query(
                            this.findBookCategoriesSql, Map.of("bookId", id), this.categoryMapper);
            book.setCategories(new HashSet<>(categories));
        }
        return book;
    }
}
