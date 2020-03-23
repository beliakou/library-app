package com.smartexlab.libraryapp.repository;

import com.smartexlab.libraryapp.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "author", path = "authors")
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
