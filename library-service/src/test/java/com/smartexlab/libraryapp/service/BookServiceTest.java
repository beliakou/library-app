package com.smartexlab.libraryapp.service;

import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

    @Mock BookRepository bookRepository;

    @InjectMocks BookService bookService;

    @Test
    void testFoundBookDtosReturned() {
        BookDto expectedBookDto1 = new BookDto();
        BookDto expectedBookDto2 = new BookDto();

        when(bookRepository.findAll()).thenReturn(List.of(expectedBookDto1, expectedBookDto2));

        List<BookDto> bookDtos = this.bookService.findBookDtos();

        Assertions.assertEquals(2, bookDtos.size());
        assertTrue(bookDtos.containsAll(List.of(expectedBookDto1, expectedBookDto2)));
    }

    @Test
    void testEmptyListReturnedWhenBooksNotFound() {
        when(bookRepository.findAll()).thenReturn(null);

        List<BookDto> bookDtos = this.bookService.findBookDtos();

        assertNotNull(bookDtos);
        assertTrue(bookDtos.isEmpty());
    }

    @Test
    void testFoundBookIsReturned() {
        Book expectedBook = new Book();
        long bookId = 12L;
        when(bookRepository.findById(bookId)).thenReturn(expectedBook);

        Book bookById = this.bookService.findBookById(bookId);

        assertEquals(expectedBook, bookById);
    }

    @Test
    void testNullReturnedWhenBookNotFound() {
        when(bookRepository.findById(any())).thenReturn(null);

        Book bookById = this.bookService.findBookById(12L);

        assertNull(null);
    }

    @Test
    void testExceptionThrownWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> this.bookService.findBookById(null));
    }
}
