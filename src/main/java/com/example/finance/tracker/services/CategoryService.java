package com.example.finance.tracker.services;

import com.example.finance.tracker.models.Category;
import com.example.finance.tracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category createCategory(Category category) {
        String normalizedName = category.getName().toLowerCase();
        Optional<Category> existingCategory = categoryRepository.findByName(normalizedName);

        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Uma categoria com o nome '" + category.getName() + "' já existe.");
        }

        category.setName(normalizedName);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if(categoryRepository.findById(id).isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Está categoria não existe no banco de dados.");
        }
    }

    @Transactional
    public void updateCategory(Category categoryUpdates, Long id) { // Renomeado para clareza
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);

        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();

            existingCategory.setName(categoryUpdates.getName());

            categoryRepository.save(existingCategory);
        } else {
            throw new IllegalArgumentException("Esta categoria não existe no banco de dados");
        }
    }
}