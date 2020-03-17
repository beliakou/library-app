package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.model.Book;
import com.smartexlab.libraryapp.model.BookDto;
import com.smartexlab.libraryapp.model.Category;
import com.smartexlab.libraryapp.repository.mapper.BookDtoMapper;
import com.smartexlab.libraryapp.repository.mapper.BookMapper;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class JdbcBookRepositoryTest {

    private static final String FIND_BOOK_DTOS = "find book dto";
    private static final String FIND_BOOK_BY_ID = "find book by id";
    private static final String FIND_BOOK_CATEGORIES = "find book categories";

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

        when(jdbcTemplate.queryForObject(FIND_BOOK_BY_ID, Map.of("bookId", bookId), bookMapper))
                .thenReturn(expectedBook);
        when(jdbcTemplate.query(FIND_BOOK_CATEGORIES, Map.of("bookId", bookId), categoryMapper))
                .thenReturn(List.of(expectedCategory1, expectedCategory2));

        Book book = this.repository.findById(bookId);

        assertNotNull(book);
        assertNotNull(book.getCategories());
        assertEquals(2, book.getCategories().size());
        assertTrue(book.getCategories().containsAll(List.of(expectedCategory1, expectedCategory2)));
    }
}
