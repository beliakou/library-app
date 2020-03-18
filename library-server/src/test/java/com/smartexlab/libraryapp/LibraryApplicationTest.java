package com.smartexlab.libraryapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(
        classes = {LibraryApplication.class, TestConfig.class},
        initializers = LibraryApplicationTest.Initializer.class)
@Testcontainers
class LibraryApplicationTest {

    private static final String BASIC_AUTH_HEADER = "Basic dXNlcjpwYXNzd29yZA==";

    @Container
    public static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer("postgres:11.7")
                    .withDatabaseName("library")
                    .withUsername("test")
                    .withPassword("test");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired MockMvc mockMvc;

    @Test
    void testUnauthorizedAccessRestricted() throws Exception {
        mockMvc.perform(get("/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthorizedAccessAllowed() throws Exception {
        mockMvc.perform(get("/books").header("Authorization", BASIC_AUTH_HEADER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void testFindBooksResponseIsCorrect() throws Exception {
        String expectedJson = IOUtils.resourceToString("/json/findBookDtos.json", StandardCharsets.UTF_8);
        mockMvc.perform(get("/books").header("Authorization", BASIC_AUTH_HEADER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void testFindBookByIdResponseIsCorrect() throws Exception {
        String expectedJson = IOUtils.resourceToString("/json/findBookById.json", StandardCharsets.UTF_8);
        mockMvc.perform(get("/books/1").header("Authorization", BASIC_AUTH_HEADER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, false))
                .andExpect(jsonPath("$.updateTime").exists());
    }
}
