package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.model.domain.Author;
import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import com.smartexlab.libraryapp.model.domain.Category;
import com.smartexlab.libraryapp.repository.mapper.BookDtoMapper;
import com.smartexlab.libraryapp.repository.mapper.BookMapper;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
import com.smartexlab.libraryapp.repository.util.ParamNames;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class JdbcBookRepositoryTest {

    private static final String FIND_BOOK_DTOS = "find book dto";
    private static final String FIND_BOOK_BY_ID = "find book by id";
    private static final String FIND_BOOK_CATEGORIES = "find book categories";
    private static final String CREATE_BOOK = "create book";
    private static final String UPDATE_BOOK = "update book";
    private static final String DELETE_BOOK_CATEGORY = "delete book category";
    private static final String CREATE_BOOK_CATEGORY = "create book category";

    @Mock private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock private BookDtoMapper bookDtoMapper;
    @Mock private BookMapper bookMapper;
    @Mock private CategoryMapper categoryMapper;

    @InjectMocks private JdbcBookRepository repository;

    @BeforeEach
    void setUp() {
        Mockito.reset(jdbcTemplate, bookDtoMapper, bookMapper, categoryMapper);
        ReflectionTestUtils.setField(repository, "bookDtoFindAllSql", FIND_BOOK_DTOS);
        ReflectionTestUtils.setField(repository, "findBookByIdSql", FIND_BOOK_BY_ID);
        ReflectionTestUtils.setField(repository, "findBookCategoriesSql", FIND_BOOK_CATEGORIES);
        ReflectionTestUtils.setField(repository, "createBookSql", CREATE_BOOK);
        ReflectionTestUtils.setField(repository, "updateBookSql", UPDATE_BOOK);
        ReflectionTestUtils.setField(
                repository, "deleteBookCategoryByBookIdSql", DELETE_BOOK_CATEGORY);
        ReflectionTestUtils.setField(repository, "createBookCategorySql", CREATE_BOOK_CATEGORY);
    }

    @Test
    void testFindAll() {
        BookDto expectedBookDto1 = new BookDto();
        BookDto expectedBookDto2 = new BookDto();
        when(jdbcTemplate.query(FIND_BOOK_DTOS, this.bookDtoMapper))
                .thenReturn(List.of(expectedBookDto1, expectedBookDto2));

        List<BookDto> bookDtos = this.repository.findAll();

        assertEquals(2, bookDtos.size());
        assertTrue(bookDtos.containsAll(List.of(expectedBookDto1, expectedBookDto2)));
    }

    @Test
    void testFindById() {
        Long bookId = 12L;
        Book expectedBook = new Book();
        expectedBook.setCategories(new HashSet<>());

        Category expectedCategory1 = new Category();
        Category expectedCategory2 = new Category();

        when(jdbcTemplate.queryForObject(
                        FIND_BOOK_BY_ID, Map.of(ParamNames.BOOK_ID, bookId), bookMapper))
                .thenReturn(expectedBook);
        when(jdbcTemplate.query(
                        FIND_BOOK_CATEGORIES, Map.of(ParamNames.BOOK_ID, bookId), categoryMapper))
                .thenReturn(List.of(expectedCategory1, expectedCategory2));

        Book book = this.repository.findById(bookId);

        assertNotNull(book);
        assertNotNull(book.getCategories());
        assertEquals(2, book.getCategories().size());
        assertTrue(book.getCategories().containsAll(List.of(expectedCategory1, expectedCategory2)));
    }

    @Test
    void testCreateBook() {
        Book book = buildBook();

        when(jdbcTemplate.update(
                        eq(CREATE_BOOK),
                        any(MapSqlParameterSource.class),
                        any(KeyHolder.class),
                        any(String[].class)))
                .then(
                        invocationOnMock -> {
                            invocationOnMock
                                    .getArgument(2, GeneratedKeyHolder.class)
                                    .getKeyList()
                                    .add(Map.of("book_id", 5L));
                            return 1;
                        });

        when(jdbcTemplate.update(eq(CREATE_BOOK_CATEGORY), anyMap())).thenReturn(1);

        Long createdBookId = this.repository.createBook(book);

        Mockito.verify(jdbcTemplate, times(1))
                .update(
                        eq(CREATE_BOOK),
                        any(MapSqlParameterSource.class),
                        any(KeyHolder.class),
                        any(String[].class));

        Mockito.verify(jdbcTemplate, times(2)).update(eq(CREATE_BOOK_CATEGORY), anyMap());
        Assertions.assertEquals(5L, createdBookId);
    }

    @Test
    void testUpdateBook() {
        Book book = buildBook();
        book.setId(12L);

        when(jdbcTemplate.update(eq(UPDATE_BOOK), any(MapSqlParameterSource.class))).thenReturn(1);

        when(jdbcTemplate.update(eq(DELETE_BOOK_CATEGORY), anyMap())).thenReturn(1);
        when(jdbcTemplate.update(eq(CREATE_BOOK_CATEGORY), anyMap())).thenReturn(1);

        this.repository.updateBook(book);

        Mockito.verify(jdbcTemplate, times(1))
                .update(eq(UPDATE_BOOK), any(MapSqlParameterSource.class));

        Mockito.verify(jdbcTemplate, times(1)).update(eq(DELETE_BOOK_CATEGORY), anyMap());
        Mockito.verify(jdbcTemplate, times(2)).update(eq(CREATE_BOOK_CATEGORY), anyMap());
    }

    private Book buildBook() {
        var book = new Book();
        book.setIsbn("12345");
        book.setName("Test book");

        var author = new Author();
        author.setId(1L);
        author.setName("Test author");

        book.setAuthor(author);

        var category1 = new Category();
        category1.setId(1L);
        category1.setName("Test category 1");

        var category2 = new Category();
        category2.setId(2L);
        category2.setName("Test category 2");

        book.setCategories(Set.of(category1, category2));

        return book;
    }
}
