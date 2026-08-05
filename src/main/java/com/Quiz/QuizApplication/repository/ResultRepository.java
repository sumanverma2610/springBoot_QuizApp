package com.Quiz.QuizApplication.repository;

import com.Quiz.QuizApplication.entity.Result;
import com.Quiz.QuizApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByUser(User user);

}