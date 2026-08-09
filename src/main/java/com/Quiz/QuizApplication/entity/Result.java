package com.Quiz.QuizApplication.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    private int totalQuestions;

    private LocalDate quizDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // NEW: Category of the quiz
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Default Constructor
    public Result() {
    }

    // Parameterized Constructor
    public Result(
            Long id,
            int score,
            int totalQuestions,
            LocalDate quizDate,
            User user,
            Category category) {

        this.id = id;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.quizDate = quizDate;
        this.user = user;
        this.category = category;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public LocalDate getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(LocalDate quizDate) {
        this.quizDate = quizDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // NEW: Category getter/setter

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}