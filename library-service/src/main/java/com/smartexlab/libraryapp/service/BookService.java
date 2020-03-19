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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;

    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
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

    public Long createBook(CreateBookRequest createBookRequest) {
        Author author = this.authorRepository.findById(createBookRequest.getAuthorId());
        Set<Category> categories = findCategories(createBookRequest.getCategories());

        Book book = new Book();

        book.setIsbn(createBookRequest.getIsbn());
        book.setName(createBookRequest.getName());
        book.setAuthor(author);
        book.setCategories(categories);

        return this.bookRepository.createBook(book);
    }

    public void updateBook(Long bookId, UpdateBookRequest updateBookRequest) {
        Author author = this.authorRepository.findById(updateBookRequest.getAuthorId());
        Set<Category> categories = findCategories(updateBookRequest.getCategories());

        Book book = new Book();
        book.setId(bookId);
        book.setIsbn(updateBookRequest.getIsbn());
        book.setName(updateBookRequest.getName());
        book.setAuthor(author);
        book.setCategories(categories);

        this.bookRepository.updateBook(book);
    }

    private Set<Category> findCategories(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(
                        categoryId ->
                                CompletableFuture.supplyAsync(
                                        () -> this.categoryRepository.findById(categoryId)))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toSet());
    }
}
