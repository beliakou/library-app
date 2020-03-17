package com.smartex.libraryapp.api.service;

import com.smartex.libraryapp.model.Category;

import java.util.List;

public interface CategoryService {
    Category findCategoryById(Long id);

    List<Category> findCategories();
}
