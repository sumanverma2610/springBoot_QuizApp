package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuestionService questionService;

    public AdminController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Open Add Question Page
    @GetMapping("/add-question")
    public String addQuestionPage(Model model) {

        model.addAttribute("question", new Question());

        return "add-question";
    }

    // Save Question
    @PostMapping("/save-question")
    public String saveQuestion(@ModelAttribute Question question) {

        questionService.saveQuestion(question);

        return "redirect:/admin/questions";
    }

    // Show All Questions
    @GetMapping("/questions")
    public String showQuestions(Model model) {

        model.addAttribute("questions",
                questionService.getAllQuestions());

        return "questions";
    }

    // Delete Question
    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {

        questionService.deleteQuestion(id);

        return "redirect:/admin/questions";
    }
}