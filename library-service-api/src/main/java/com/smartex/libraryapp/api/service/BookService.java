package com.smartex.libraryapp.api.service;

import com.smartex.libraryapp.model.Author;
import com.smartex.libraryapp.model.Book;

import java.util.List;

public interface BookService {
    Book findBookById(Long id);

    List<Book> findBooksByAuthor(Author author);

    List<Book> findBooks();
}
