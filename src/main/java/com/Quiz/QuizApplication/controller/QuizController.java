package com.Quiz.QuizApplication.controller;

import java.security.Principal;
import java.time.LocalDate;

import com.Quiz.QuizApplication.entity.Result;
import com.Quiz.QuizApplication.entity.User;
import com.Quiz.QuizApplication.service.ResultService;
import com.Quiz.QuizApplication.service.UserService;
import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.service.ResultService;
import com.Quiz.QuizApplication.service.UserService;
import org.springframework.ui.Model;
import com.Quiz.QuizApplication.service.QuestionService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    private final QuestionService questionService;
    private final ResultService resultService ;
    private final UserService userService;
    public QuizController(QuestionService questionService, ResultService resultService, UserService userService) {
        this.questionService=questionService;
        this.resultService = resultService;
        this.userService = userService;
    }

    @GetMapping("/quiz")
    public String quiz(Model model){
        model.addAttribute("questions",
                questionService.getAllQuestions());

        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(@RequestParam Map<String, String> answers,
                             Model model,
                             Principal principal) {

        List<Question> questions = questionService.getAllQuestions();

        int score = 0;

        for (Question question : questions) {

            String selectedAnswer = answers.get("question_" + question.getId());

            if (selectedAnswer != null &&
                    selectedAnswer.equals(question.getCorrectAnswer())) {
                score++;
            }
        }

        User user = userService.findByUsername(principal.getName());

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
    @GetMapping("/my-results")
    public String myResults(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());

        model.addAttribute("results", resultService.getResultsByUser(user));

        return "my-results";
    }
}


