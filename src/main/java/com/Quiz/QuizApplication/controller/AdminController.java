package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.service.CategoryService;
import com.Quiz.QuizApplication.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuestionService questionService;
    private final CategoryService categoryService;

    public AdminController(
            QuestionService questionService,
            CategoryService categoryService) {

        this.questionService = questionService;
        this.categoryService = categoryService;
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
}