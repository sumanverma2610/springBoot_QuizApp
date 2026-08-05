package com.Quiz.QuizApplication.service;

import com.Quiz.QuizApplication.entity.Question;
import com.Quiz.QuizApplication.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Add Question
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Get All Questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Get Question By Id
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    // Update Question
    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Delete Question
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}