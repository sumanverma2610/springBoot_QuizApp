package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.entity.Category;
import com.Quiz.QuizApplication.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Show categories
    @GetMapping
    public String categories(Model model) {

        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        model.addAttribute(
                "category",
                new Category()
        );

        return "categories";
    }

    // Save category
    @PostMapping("/save")
    public String saveCategory(
            @ModelAttribute Category category) {

        categoryService.saveCategory(category);

        return "redirect:/admin/categories";
    }

    // Delete category
    @GetMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return "redirect:/admin/categories";
    }
}