package com.smartexlab.libraryapp.api.respository;

import com.smartexlab.libraryapp.model.domain.Author;

public interface AuthorRepository {
    Author findById(Long id);
}
