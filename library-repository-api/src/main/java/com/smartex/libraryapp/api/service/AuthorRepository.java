package com.smartex.libraryapp.api.service;

import com.smartex.libraryapp.model.Author;

import java.util.List;

public interface AuthorRepository {
    Author findAuthorById(Long id);

    List<Author> findAuthorsByName(String name);

    List<Author> findAuthors();
}
