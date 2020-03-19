package com.smartexlab.libraryapp.repository.mapper;

import com.smartexlab.libraryapp.model.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AuthorMapperTest {

    private static final Long EXPECTED_AUTHOR_ID = 12L;
    private static final String EXPECTED_AUTHOR_NAME = "test author";

    @Mock ResultSet resultSet;

    AuthorMapper mapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(this.resultSet);
        this.mapper = new AuthorMapper();
    }

    @Test
    void testFullData() throws Exception {
        Mockito.when(resultSet.getLong("author_id")).thenReturn(EXPECTED_AUTHOR_ID);
        Mockito.when(resultSet.getString("author_name")).thenReturn(EXPECTED_AUTHOR_NAME);

        Author author = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should map all columns correctly",
                () -> assertEquals(EXPECTED_AUTHOR_ID, author.getId()),
                () -> assertEquals(EXPECTED_AUTHOR_NAME, author.getName()));
    }

    @Test
    void testNoData() throws Exception {
        Mockito.when(resultSet.getLong("author_id")).thenReturn(0L);
        Mockito.when(resultSet.getString("author_name")).thenReturn(null);

        Author author = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should leave fields empty",
                () -> assertEquals(0L, author.getId()),
                () -> assertNull(author.getName()));
    }
}
