package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.dto.AIQuestionDTO;
import com.Quiz.QuizApplication.entity.Question;
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


    public AIController(
            GeminiService geminiService,
            QuestionService questionService) {

        this.geminiService = geminiService;
        this.questionService = questionService;
    }


    // ==========================================
    // Existing AI Test Page
    // ==========================================

    @GetMapping("/test")
    public String testPage() {

        return "ai-test";
    }


    // ==========================================
    // Existing AI Test
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
    // AI Quiz Generator Page
    // ==========================================

    @GetMapping("/quiz")
    public String quizPage() {

        return "ai-quiz";
    }


    // ==========================================
    // Generate AI Quiz
    // ==========================================

    @PostMapping("/quiz/generate")
    public String generateQuiz(
            @RequestParam String topic,
            @RequestParam int numberOfQuestions,
            Model model) {

        try {

            List<AIQuestionDTO> aiQuestions =
                    geminiService.generateQuizQuestions(
                            topic,
                            numberOfQuestions
                    );

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

                // Category is currently null
                question.setCategory(null);

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

        return "ai-quiz";
    }
}