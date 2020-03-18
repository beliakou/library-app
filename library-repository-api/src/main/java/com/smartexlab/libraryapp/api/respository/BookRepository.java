package com.smartexlab.libraryapp.api.respository;


import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;

import java.util.List;

public interface BookRepository {
    List<BookDto> findAll();

    Book findById(Long id);
}
