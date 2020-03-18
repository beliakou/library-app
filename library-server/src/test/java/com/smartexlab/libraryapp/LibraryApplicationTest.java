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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = LibraryApplicationTest.Initializer.class)
@Testcontainers
class LibraryApplicationTest {

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
}
