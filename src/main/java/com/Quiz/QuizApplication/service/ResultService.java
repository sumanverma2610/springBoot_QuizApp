package com.Quiz.QuizApplication.service;

import com.Quiz.QuizApplication.entity.Result;
import com.Quiz.QuizApplication.entity.User;
import com.Quiz.QuizApplication.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    // Save Quiz Result
    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    // Get All Results
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    // Get Result By Id
    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    // Get Results Of One User
    public List<Result> getResultsByUser(User user) {
        return resultRepository.findByUser(user);
    }

    // Delete Result
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }
    public long getTotalAttempts() { return resultRepository.count(); }

}