package com.smartexlab.libraryapp;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(
        classes = {LibraryApplication.class},
        initializers = LibraryApplicationTest.Initializer.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibraryApplicationTest {

    private static final String BASIC_AUTH_HEADER_USER = "Basic dXNlcjp1c2VyUGFzc3dvcmQ=";
    private static final String BASIC_AUTH_HEADER_ADMIN = "Basic YWRtaW46YWRtaW5QYXNzd29yZA==";

    @Container
    public static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer("postgres:11.7")
                    .withDatabaseName("library")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired MockMvc mockMvc;

    @Test
    void testUnauthorizedAccessRestricted() throws Exception {
        mockMvc.perform(get("/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthorizedAccessAllowed() throws Exception {
        mockMvc.perform(get("/books").header("Authorization", BASIC_AUTH_HEADER_USER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateBookWithUserRoleRestricted() throws Exception {
        String createBookJson =
                IOUtils.resourceToString("/json/createBookRequest.json", StandardCharsets.UTF_8);
        mockMvc.perform(
                        post("/books")
                                .content(createBookJson)
                                .header("Authorization", BASIC_AUTH_HEADER_USER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateBookWithUserRoleRestricted() throws Exception {
        String createBookJson =
                IOUtils.resourceToString("/json/updateBookRequest.json", StandardCharsets.UTF_8);
        mockMvc.perform(
                        put("/books/1")
                                .content(createBookJson)
                                .header("Authorization", BASIC_AUTH_HEADER_USER)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(0)
    void testListOfBooksWithNameAndAuthorIsReturned() throws Exception {
        String expectedJson =
                IOUtils.resourceToString("/json/findBookDtos.json", StandardCharsets.UTF_8);
        mockMvc.perform(get("/books").header("Authorization", BASIC_AUTH_HEADER_USER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, false));
    }

    @Test
    @Order(0)
    void testBookWithDetailsIsReturned() throws Exception {
        String expectedJson =
                IOUtils.resourceToString("/json/findBookById.json", StandardCharsets.UTF_8);
        mockMvc.perform(get("/books/1").header("Authorization", BASIC_AUTH_HEADER_USER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, false));
    }

    @Test
    @Order(0)
    void testBookUpdateTimeIsReturned() throws Exception {
        mockMvc.perform(get("/books/1").header("Authorization", BASIC_AUTH_HEADER_USER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updateTime").exists());
    }

    @Test
    @Order(0)
    void testErrorReturnedWhenBookNotFound() throws Exception {
        String expectedJson =
                IOUtils.resourceToString("/json/bookNotFound.json", StandardCharsets.UTF_8);
        mockMvc.perform(get("/books/22").header("Authorization", BASIC_AUTH_HEADER_USER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void testUpdateTimeIsChangedAfterUpdate() throws Exception {
        String bookUri = "/books/1";
        String contentBeforeUpdate =
                mockMvc.perform(get(bookUri).header("Authorization", BASIC_AUTH_HEADER_USER))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        String updateBookJson =
                IOUtils.resourceToString("/json/updateBookRequest.json", StandardCharsets.UTF_8);

        mockMvc.perform(
                        put(bookUri)
                                .content(updateBookJson)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BASIC_AUTH_HEADER_ADMIN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        String contentAfterUpdate =
                mockMvc.perform(get(bookUri).header("Authorization", BASIC_AUTH_HEADER_USER))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        assertNotEquals(
                JsonPath.<String>read(contentBeforeUpdate, "$.updateTime"),
                JsonPath.<String>read(contentAfterUpdate, "$.updateTime"));
    }

    @Test
    void testCreateBookSuccessful() throws Exception {
        String createBookJson =
                IOUtils.resourceToString("/json/createBookRequest.json", StandardCharsets.UTF_8);
        String createdBookLocation =
                mockMvc.perform(
                                post("/books")
                                        .content(createBookJson)
                                        .header("Authorization", BASIC_AUTH_HEADER_ADMIN)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        String expectedJson =
                IOUtils.resourceToString(
                        "/json/getCreatedBookResponse.json", StandardCharsets.UTF_8);
        mockMvc.perform(get(createdBookLocation).header("Authorization", BASIC_AUTH_HEADER_ADMIN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, false));
    }

    @Test
    void testUpdatedBookReturned() throws Exception {
        String createBookJson =
                IOUtils.resourceToString("/json/createBookRequest.json", StandardCharsets.UTF_8);
        String createdBookLocation =
                mockMvc.perform(
                                post("/books")
                                        .content(createBookJson)
                                        .header("Authorization", BASIC_AUTH_HEADER_ADMIN)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        String updateBookJson =
                IOUtils.resourceToString("/json/updateBookRequest.json", StandardCharsets.UTF_8);

        mockMvc.perform(
                        put(createdBookLocation)
                                .content(updateBookJson)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BASIC_AUTH_HEADER_ADMIN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        String expectedJson =
                IOUtils.resourceToString(
                        "/json/getUpdatedBookResponse.json", StandardCharsets.UTF_8);
        mockMvc.perform(get(createdBookLocation).header("Authorization", BASIC_AUTH_HEADER_ADMIN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, false));
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
