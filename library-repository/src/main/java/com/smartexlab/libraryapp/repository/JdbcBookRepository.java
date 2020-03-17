package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.Book;
import com.smartexlab.libraryapp.model.BookDto;

import java.util.List;

public class JdbcBookRepository implements BookRepository {
    @Override
    public List<BookDto> findAll() {
        return null;
    }

    @Override
    public Book findById(Long id) {
        return null;
    }
}
