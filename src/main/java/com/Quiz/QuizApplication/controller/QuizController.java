package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.entity.Category;
import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.entity.Result;
import com.Quiz.QuizApplication.entity.User;
import com.Quiz.QuizApplication.service.CategoryService;
import com.Quiz.QuizApplication.service.QuestionService;
import com.Quiz.QuizApplication.service.ResultService;
import com.Quiz.QuizApplication.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    private final QuestionService questionService;
    private final ResultService resultService;
    private final UserService userService;
    private final CategoryService categoryService;

    public QuizController(
            QuestionService questionService,
            ResultService resultService,
            UserService userService,
            CategoryService categoryService) {

        this.questionService = questionService;
        this.resultService = resultService;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    // ==========================================
    // SELECT CATEGORY
    // ==========================================

    @GetMapping("/quiz")
    public String quizCategory(Model model) {

        List<Category> categories =
                categoryService.getAllCategories();

        model.addAttribute("categories", categories);

        return "quiz-category";
    }


    // ==========================================
    // START QUIZ BY CATEGORY
    // ==========================================

    @GetMapping("/quiz/start")
    public String startQuiz(
            @RequestParam Long categoryId,
            Model model) {

        List<Question> questions =
                questionService.getQuestionsByCategory(categoryId);

        Category category =
                categoryService
                        .getCategoryById(categoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                )
                        );

        model.addAttribute("questions", questions);
        model.addAttribute("category", category);

        return "quiz";
    }


    // ==========================================
    // SUBMIT QUIZ
    // ==========================================

    @PostMapping("/submitQuiz")
    public String submitQuiz(
            @RequestParam Map<String, String> answers,
            @RequestParam Long categoryId,
            Model model,
            Principal principal) {

        List<Question> questions =
                questionService.getQuestionsByCategory(categoryId);

        int score = 0;

        for (Question question : questions) {

            String selectedAnswer =
                    answers.get("question_" + question.getId());

            if (selectedAnswer != null &&
                    selectedAnswer.equals(
                            question.getCorrectAnswer())) {

                score++;
            }
        }

        User user =
                userService.findByUsername(
                        principal.getName()
                );

        Result result = new Result();

        result.setUser(user);
        result.setScore(score);
        result.setTotalQuestions(questions.size());
        result.setQuizDate(LocalDate.now());

        resultService.saveResult(result);

        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());

        return "result";
    }


    // ==========================================
    // MY RESULTS
    // ==========================================

    @GetMapping("/my-results")
    public String myResults(
            Model model,
            Principal principal) {

        User user =
                userService.findByUsername(
                        principal.getName()
                );

        model.addAttribute(
                "results",
                resultService.getResultsByUser(user)
        );

        return "my-results";
    }
}