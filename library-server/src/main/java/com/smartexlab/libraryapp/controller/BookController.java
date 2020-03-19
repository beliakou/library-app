package com.smartexlab.libraryapp.controller;

import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import com.smartexlab.libraryapp.model.request.CreateBookRequest;
import com.smartexlab.libraryapp.model.request.UpdateBookRequest;
import com.smartexlab.libraryapp.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.bookService.findBookById(id));
    }

    @PostMapping(
            value = "/books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBook(@RequestBody CreateBookRequest createBookRequest) {
        Long createdBookId = this.bookService.createBook(createBookRequest);
        return ResponseEntity.created(URI.create(String.format("/books/%s", createdBookId)))
                .build();
    }

    @PutMapping(
            value = "/books/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBook(
            @PathVariable("id") Long id, @RequestBody UpdateBookRequest updateBookRequest) {
        this.bookService.updateBook(id, updateBookRequest);
        return ResponseEntity.noContent().build();
    }
}
