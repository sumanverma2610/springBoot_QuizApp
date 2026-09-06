package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.entity.Category;
import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.service.CategoryService;
import com.Quiz.QuizApplication.service.QuestionService;
import com.Quiz.QuizApplication.service.ResultService;
import com.Quiz.QuizApplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ResultService resultService;

    public AdminController(
            QuestionService questionService,
            CategoryService categoryService ,UserService userService,ResultService resultService ) {

        this.questionService = questionService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.resultService = resultService;
    }

    // Open Add Question Page
    @GetMapping("/add-question")
    public String addQuestionPage(Model model) {

        model.addAttribute("question", new Question());

        // Send categories to HTML
        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "add-question";
    }

    // Save Question
    // NOTE: path matches the form's th:action="@{/admin/add-question}" in
    // add-question.html - it previously posted to "/save", which had no
    // matching handler and returned a 405.
    @PostMapping("/add-question")
    public String saveQuestion(
            @ModelAttribute Question question,
            @RequestParam Long categoryId) {

        // The form sends "categoryId" (a Long), but Question only exposes
        // setCategory(Category) - Spring can't bind that field automatically,
        // so we resolve it ourselves and attach it before saving.
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        question.setCategory(category);

        questionService.saveQuestion(question);

        return "redirect:/admin/questions";
    }

    // Show All Questions
    @GetMapping("/questions")
    public String showQuestions(Model model) {

        model.addAttribute(
                "questions",
                questionService.getAllQuestions()
        );

        return "questions";
    }

    // Delete Question
    @GetMapping("/delete/{id}")
    public String deleteQuestion(
            @PathVariable Long id) {

        questionService.deleteQuestion(id);

        return "redirect:/admin/questions";
    }
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        model.addAttribute(
                "totalStudents",
                userService.getTotalStudents()
        );

        model.addAttribute(
                "totalQuestions",
                questionService.getTotalQuestions()
        );

        model.addAttribute(
                "totalCategories",
                categoryService.getTotalCategories()
        );

        model.addAttribute(
                "totalAttempts",
                resultService.getTotalAttempts()
        );

        return "admin-dashboard";
    }
}