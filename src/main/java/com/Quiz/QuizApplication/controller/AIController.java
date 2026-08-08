package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.dto.AIQuestionDTO;
import com.Quiz.QuizApplication.entity.Category;
import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.service.CategoryService;
import com.Quiz.QuizApplication.service.GeminiService;
import com.Quiz.QuizApplication.service.QuestionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ai")
public class AIController {

    private final GeminiService geminiService;
    private final QuestionService questionService;
    private final CategoryService categoryService;

    public AIController(
            GeminiService geminiService,
            QuestionService questionService,
            CategoryService categoryService) {

        this.geminiService = geminiService;
        this.questionService = questionService;
        this.categoryService = categoryService;
    }


    // ==========================================
    // AI Test Page
    // ==========================================

    @GetMapping("/test")
    public String testPage() {
        return "ai-test";
    }


    // ==========================================
    // AI Test
    // ==========================================

    @PostMapping("/generate")
    public String generate(
            @RequestParam String prompt,
            Model model) {

        String response =
                geminiService.generateContent(prompt);

        model.addAttribute("response", response);

        return "ai-test";
    }


    // ==========================================
    // AI Quiz Page
    // ==========================================

    @GetMapping("/quiz")
    public String quizPage(Model model) {

        // Send categories to HTML
        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "ai-quiz";
    }


    // ==========================================
    // Generate AI Quiz
    // ==========================================

    @PostMapping("/quiz/generate")
    public String generateQuiz(
            @RequestParam String topic,
            @RequestParam int numberOfQuestions,
            @RequestParam Long categoryId,
            Model model) {

        try {

            // Find selected category
            Category category =
                    categoryService
                            .getCategoryById(categoryId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Category not found"
                                    )
                            );


            // Generate questions using Gemini
            List<AIQuestionDTO> aiQuestions =
                    geminiService.generateQuizQuestions(
                            topic,
                            numberOfQuestions
                    );


            // Save every question
            for (AIQuestionDTO aiQuestion : aiQuestions) {

                Question question = new Question();

                question.setQuestionTitle(
                        aiQuestion.getQuestionTitle()
                );

                question.setOptionA(
                        aiQuestion.getOptionA()
                );

                question.setOptionB(
                        aiQuestion.getOptionB()
                );

                question.setOptionC(
                        aiQuestion.getOptionC()
                );

                question.setOptionD(
                        aiQuestion.getOptionD()
                );

                question.setCorrectAnswer(
                        aiQuestion.getCorrectAnswer()
                );


                // IMPORTANT
                // Attach selected category
                question.setCategory(category);


                // Save to database
                questionService.saveQuestion(question);
            }


            model.addAttribute(
                    "message",
                    aiQuestions.size()
                            + " questions generated and saved successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute(
                    "error",
                    "Failed to generate questions: "
                            + e.getMessage()
            );
        }


        // Load categories again
        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "ai-quiz";
    }
}