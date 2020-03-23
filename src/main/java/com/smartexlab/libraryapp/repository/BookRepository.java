package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.domain.Author;
import com.smartexlab.libraryapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;

@RepositoryRestResource(collectionResourceRel = "book", path = "books", excerptProjection = BookDto.class)
public interface BookRepository extends JpaRepository<Book, Long> {
}

@Projection(name = "bookDto", types = { Book.class })
interface BookDto {

    String getName();

    Author getAuthor();
}
