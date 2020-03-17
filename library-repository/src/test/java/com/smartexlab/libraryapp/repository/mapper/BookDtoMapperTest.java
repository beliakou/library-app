package com.smartexlab.libraryapp.repository.mapper;

import com.smartexlab.libraryapp.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class BookDtoMapperTest {

    private static final Long EXPECTED_BOOK_ID = 12L;
    private static final String EXPECTED_BOOK_NAME = "test book";
    private static final String EXPECTED_AUTHOR_NAME = "test author";

    @Mock ResultSet resultSet;

    BookDtoMapper mapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(this.resultSet);
        this.mapper = new BookDtoMapper();
    }

    @Test
    void testFullData() throws Exception {
        Mockito.when(resultSet.getLong("book_id")).thenReturn(EXPECTED_BOOK_ID);
        Mockito.when(resultSet.getString("book_name")).thenReturn(EXPECTED_BOOK_NAME);
        Mockito.when(resultSet.getString("author_name")).thenReturn(EXPECTED_AUTHOR_NAME);

        BookDto book = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should map all columns correctly",
                () -> assertEquals(EXPECTED_BOOK_ID, book.getId()),
                () -> assertEquals(EXPECTED_BOOK_NAME, book.getBookName()),
                () -> assertEquals(EXPECTED_AUTHOR_NAME, book.getAuthorName()));
    }

    @Test
    void testNoData() throws Exception {
        Mockito.when(resultSet.getLong("book_id")).thenReturn(0L);
        Mockito.when(resultSet.getString("book_name")).thenReturn(null);
        Mockito.when(resultSet.getString("author_name")).thenReturn(null);

        BookDto book = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should leave fields empty",
                () -> assertEquals(0L, book.getId()),
                () -> assertNull(book.getBookName()),
                () -> assertNull(book.getAuthorName()));
    }
}
