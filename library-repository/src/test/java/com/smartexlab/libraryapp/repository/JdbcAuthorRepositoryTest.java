package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.model.domain.Author;
import com.smartexlab.libraryapp.repository.mapper.AuthorMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

@ExtendWith(SpringExtension.class)
class JdbcAuthorRepositoryTest {
    private static final String FIND_AUTHOR = "find author";

    @Mock private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock private AuthorMapper authorMapper;

    @InjectMocks private JdbcAuthorRepository repository;

    @BeforeEach
    void setUp() {
        Mockito.reset(jdbcTemplate, authorMapper);
        ReflectionTestUtils.setField(repository, "findAuthorByIdSql", FIND_AUTHOR);
    }

    @Test
    void testAuthorReturned() {
        Author expectedAuthor = new Author();
        long authorId = 12L;
        Mockito.when(
                        jdbcTemplate.queryForObject(
                                FIND_AUTHOR, Map.of("authorId", authorId), authorMapper))
                .thenReturn(expectedAuthor);

        Author author = repository.findById(authorId);
        Assertions.assertEquals(expectedAuthor, author);
    }
}
