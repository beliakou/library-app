package com.smartexlab.libraryapp.api.respository;

import com.smartexlab.libraryapp.model.domain.Category;

public interface CategoryRepository {
    Category findById(Long id);
}
