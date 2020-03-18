package com.smartexlab.libraryapp.service;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> findBookDtos() {
        return Objects.requireNonNullElse(this.bookRepository.findAll(), Collections.emptyList());
    }

    public Book findBookById(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book id cannot be null");
        }

        return this.bookRepository.findById(bookId);
    }
}
