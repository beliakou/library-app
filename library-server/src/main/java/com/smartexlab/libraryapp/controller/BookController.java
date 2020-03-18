package com.smartexlab.libraryapp.controller;

import com.smartexlab.libraryapp.model.BookDto;
import com.smartexlab.libraryapp.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> findBookDtos() {
        return ResponseEntity.ok(this.bookService.findBookDtos());
    }

}
