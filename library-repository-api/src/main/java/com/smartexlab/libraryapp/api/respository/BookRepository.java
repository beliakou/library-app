package com.smartexlab.libraryapp.api.respository;


import com.smartexlab.libraryapp.model.Book;
import com.smartexlab.libraryapp.model.BookDto;

import java.util.List;

public interface BookRepository {
    List<BookDto> findAll();

    Book findById(Long id);
}
