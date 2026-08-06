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
    @PostMapping("/save")
    public String saveQuestion(@ModelAttribute Question question) {

        System.out.println("INSIDE SAVE API");
        System.out.println("========== SAVE METHOD CALLED ==========");
        System.out.println("Question: " + question.getQuestionTitle());
        System.out.println("Option A: " + question.getOptionA());
        System.out.println("Option B: " + question.getOptionB());
        System.out.println("Correct Answer: " + question.getCorrectAnswer());

        try {
            questionService.saveQuestion(question);
            System.out.println("Question saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }

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