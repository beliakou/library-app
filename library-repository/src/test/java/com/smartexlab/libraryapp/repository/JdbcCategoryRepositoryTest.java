package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.model.domain.Category;
import com.smartexlab.libraryapp.repository.mapper.CategoryMapper;
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
class JdbcCategoryRepositoryTest {
    private static final String FIND_CATEGORY = "find category";

    @Mock private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock private CategoryMapper categoryMapper;

    @InjectMocks private JdbcCategoryRepository repository;

    @BeforeEach
    void setUp() {
        Mockito.reset(jdbcTemplate, categoryMapper);
        ReflectionTestUtils.setField(repository, "findCategoryByIdSql", FIND_CATEGORY);
    }

    @Test
    void testCategoryReturned() {
        Category expectedCategory = new Category();
        long categoryId = 12L;
        Mockito.when(
                        jdbcTemplate.queryForObject(
                                FIND_CATEGORY, Map.of("categoryId", categoryId), categoryMapper))
                .thenReturn(expectedCategory);

        Category category = repository.findById(categoryId);
        Assertions.assertEquals(expectedCategory, category);
    }
}
