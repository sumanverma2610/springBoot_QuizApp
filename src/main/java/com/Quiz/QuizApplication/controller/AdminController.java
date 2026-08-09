package com.Quiz.QuizApplication.controller;

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
    @PostMapping("/save")
    public String saveQuestion(
            @ModelAttribute Question question) {

        System.out.println("========== SAVE METHOD CALLED ==========");
        System.out.println("Question: " + question.getQuestionTitle());
        System.out.println("Option A: " + question.getOptionA());
        System.out.println("Option B: " + question.getOptionB());
        System.out.println("Correct Answer: " + question.getCorrectAnswer());

        if (question.getCategory() != null) {
            System.out.println(
                    "Category: " +
                            question.getCategory().getName()
            );
        }

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