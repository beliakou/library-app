package com.smartex.libraryapp.repository.mapper;

import com.smartex.libraryapp.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.ResultSet;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class BookMapperTest {

    public static final long EXPECTED_BOOK_ID = 12L;
    public static final String EXPECTED_BOOK_ISBN = "1234567890";
    public static final String EXPECTED_BOOK_NAME = "Test book";
    public static final long EXPECTED_AUTHOR_ID = 5L;
    public static final String EXPECTED_AUTHOR_NAME = "Test author";

    @Mock ResultSet resultSet;

    BookMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new BookMapper();
    }

    @Test
    void testFullData() throws Exception {
        Mockito.when(resultSet.getLong("book_id")).thenReturn(EXPECTED_BOOK_ID);
        Mockito.when(resultSet.getString("book_isbn")).thenReturn(EXPECTED_BOOK_ISBN);
        Mockito.when(resultSet.getString("book_name")).thenReturn(EXPECTED_BOOK_NAME);
        Mockito.when(resultSet.getLong("author_id")).thenReturn(EXPECTED_AUTHOR_ID);
        Mockito.when(resultSet.getString("author_name")).thenReturn(EXPECTED_AUTHOR_NAME);

        Book book = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should map all columns correctly",
                () -> assertEquals(EXPECTED_BOOK_ID, book.getId()),
                () -> assertEquals(EXPECTED_BOOK_ISBN, book.getIsbn()),
                () -> assertEquals(EXPECTED_BOOK_NAME, book.getName()),
                () -> assertEquals(EXPECTED_AUTHOR_ID, book.getAuthor().getId()),
                () -> assertEquals(EXPECTED_AUTHOR_NAME, book.getAuthor().getName()),
                () -> assertEquals(Collections.EMPTY_SET, book.getCategories()));
    }

    @Test
    void testNoData() throws Exception {
        Mockito.when(resultSet.getLong("book_id")).thenReturn(0L);
        Mockito.when(resultSet.getString("book_isbn")).thenReturn(null);
        Mockito.when(resultSet.getString("book_name")).thenReturn(null);
        Mockito.when(resultSet.getLong("author_id")).thenReturn(0L);
        Mockito.when(resultSet.getString("author_name")).thenReturn(null);

        Book book = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should map left fields empty",
                () -> assertEquals(0L, book.getId()),
                () -> assertNull(book.getIsbn()),
                () -> assertNull(book.getName()),
                () -> assertEquals(0L, book.getAuthor().getId()),
                () -> assertNull(book.getAuthor().getName()),
                () -> assertEquals(Collections.EMPTY_SET, book.getCategories()));
    }
}
