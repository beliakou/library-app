package com.smartexlab.libraryapp.service;

import com.smartexlab.libraryapp.api.respository.AuthorRepository;
import com.smartexlab.libraryapp.api.respository.BookRepository;
import com.smartexlab.libraryapp.api.respository.CategoryRepository;
import com.smartexlab.libraryapp.model.domain.Author;
import com.smartexlab.libraryapp.model.domain.Book;
import com.smartexlab.libraryapp.model.domain.BookDto;
import com.smartexlab.libraryapp.model.domain.Category;
import com.smartexlab.libraryapp.model.request.CreateBookRequest;
import com.smartexlab.libraryapp.model.request.UpdateBookRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

    @Mock BookRepository bookRepository;
    @Mock AuthorRepository authorRepository;
    @Mock CategoryRepository categoryRepository;

    @InjectMocks BookService bookService;

    @Captor ArgumentCaptor<Book> bookArgumentCaptor;

    @BeforeEach
    void setUp() {
        Mockito.reset(bookRepository, authorRepository, categoryRepository);
    }

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

    @Test
    void testCreateBook() {
        Author expectedAuthor = new Author();
        Category expectedCategory = new Category();
        String expectedIsbn = "1234";
        String expectedBookName = "Test book";

        long authorId = 2L;
        when(authorRepository.findById(authorId)).thenReturn(expectedAuthor);
        long categoryId = 12L;
        when(categoryRepository.findById(categoryId)).thenReturn(expectedCategory);

        var createBookRequest = new CreateBookRequest();
        createBookRequest.setIsbn(expectedIsbn);
        createBookRequest.setName(expectedBookName);
        createBookRequest.setAuthorId(authorId);
        createBookRequest.setCategories(Set.of(categoryId));

        when(bookRepository.createBook(bookArgumentCaptor.capture())).thenReturn(22L);

        Long createBookId = this.bookService.createBook(createBookRequest);

        verify(authorRepository).findById(authorId);
        verify(categoryRepository).findById(categoryId);

        assertNull(bookArgumentCaptor.getValue().getId());
        assertEquals(expectedIsbn, bookArgumentCaptor.getValue().getIsbn());
        assertEquals(expectedBookName, bookArgumentCaptor.getValue().getName());
        assertEquals(expectedAuthor, bookArgumentCaptor.getValue().getAuthor());
        assertEquals(1, bookArgumentCaptor.getValue().getCategories().size());
        assertTrue(bookArgumentCaptor.getValue().getCategories().contains(expectedCategory));

        assertEquals(createBookId, 22L);
    }

    @Test
    void testUpdateBook() {
        long authorId = 2L;
        when(authorRepository.findById(authorId)).thenReturn(new Author());
        long categoryId1 = 12L;
        long categoryId2 = 13L;
        when(categoryRepository.findById(any())).thenReturn(new Category());

        var updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setIsbn("1234");
        updateBookRequest.setName("Test book");
        updateBookRequest.setAuthorId(authorId);
        updateBookRequest.setCategories(Set.of(categoryId1, categoryId2));

        this.bookService.updateBook(5L, updateBookRequest);

        verify(authorRepository).findById(authorId);
        verify(categoryRepository).findById(categoryId1);
        verify(categoryRepository).findById(categoryId2);
    }
}
