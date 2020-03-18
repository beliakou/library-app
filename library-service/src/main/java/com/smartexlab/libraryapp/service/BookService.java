package com.smartexlab.libraryapp.service;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.Book;
import com.smartexlab.libraryapp.model.BookDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> findBookDtos() {
        return Optional.ofNullable(this.bookRepository.findAll()).orElse(Collections.emptyList());
    }

    public Optional<Book> findBookById(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book id cannot be null");
        }

        return Optional.ofNullable(this.bookRepository.findById(bookId));
    }
}
