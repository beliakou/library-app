package com.smartexlab.libraryapp.repository.mapper;

import com.smartexlab.libraryapp.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CategoryMapperTest {

    private static final Long EXPECTED_CATEGORY_ID = 22L;
    private static final String EXPECTED_CATEGORY_NAME = "test category";

    @Mock ResultSet resultSet;

    CategoryMapper mapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(this.resultSet);
        this.mapper = new CategoryMapper();
    }

    @Test
    void testFullData() throws Exception {
        Mockito.when(resultSet.getLong("category_id")).thenReturn(EXPECTED_CATEGORY_ID);
        Mockito.when(resultSet.getString("category_name")).thenReturn(EXPECTED_CATEGORY_NAME);

        Category category = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should map all columns correctly",
                () -> assertEquals(EXPECTED_CATEGORY_ID, category.getId()),
                () -> assertEquals(EXPECTED_CATEGORY_NAME, category.getName()));
    }

    @Test
    void testNoData() throws Exception {
        Mockito.when(resultSet.getLong("category_id")).thenReturn(0L);
        Mockito.when(resultSet.getString("category_name")).thenReturn(null);

        Category category = this.mapper.mapRow(resultSet, 0);

        assertAll(
                "Should leave columns empty",
                () -> assertEquals(0L, category.getId()),
                () -> assertNull(category.getName()));
    }
}
