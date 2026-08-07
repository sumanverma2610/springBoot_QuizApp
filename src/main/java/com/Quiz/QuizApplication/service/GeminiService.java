package com.Quiz.QuizApplication.service;


import com.Quiz.QuizApplication.dto.AIQuestionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();


    // ==========================================
    // Normal AI response
    // ==========================================

    public String generateContent(String prompt) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model
                + ":generateContent?key="
                + apiKey;

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        url,
                        requestBody,
                        Map.class
                );

        Map responseBody = response.getBody();

        if (responseBody == null) {
            return "No response received from Gemini.";
        }

        List candidates = (List) responseBody.get("candidates");

        if (candidates == null || candidates.isEmpty()) {
            return "Gemini returned no candidates.";
        }

        Map candidate = (Map) candidates.get(0);

        Map responseContent = (Map) candidate.get("content");

        if (responseContent == null) {
            return "No content received from Gemini.";
        }

        List parts = (List) responseContent.get("parts");

        if (parts == null || parts.isEmpty()) {
            return "No text received from Gemini.";
        }

        Map firstPart = (Map) parts.get(0);

        Object text = firstPart.get("text");

        if (text == null) {
            return "Gemini response does not contain text.";
        }

        return text.toString();
    }


    // ==========================================
    // Generate Quiz Questions
    // ==========================================

    public List<AIQuestionDTO> generateQuizQuestions(
            String topic,
            int numberOfQuestions) throws Exception {

        String prompt = """
                Generate %d multiple choice questions about %s.

                Each question must have exactly 4 options.

                Return ONLY valid JSON.

                Do NOT use markdown.
                Do NOT use ```json.
                Do NOT add explanations.

                JSON format:

                [
                  {
                    "questionTitle": "Question text",
                    "optionA": "Option A",
                    "optionB": "Option B",
                    "optionC": "Option C",
                    "optionD": "Option D",
                    "correctAnswer": "A"
                  }
                ]

                The correctAnswer must contain only:
                A, B, C, or D.

                Make the questions suitable for a beginner-level quiz.
                """.formatted(numberOfQuestions, topic);

        String response = generateContent(prompt);

        System.out.println("========== GEMINI QUIZ RESPONSE ==========");
        System.out.println(response);
        System.out.println("==========================================");

        // Remove markdown if Gemini still returns it
        response = cleanJsonResponse(response);

        return objectMapper.readValue(
                response,
                new TypeReference<List<AIQuestionDTO>>() {}
        );
    }


    // ==========================================
    // Clean Gemini JSON response
    // ==========================================

    private String cleanJsonResponse(String response) {

        response = response.trim();

        if (response.startsWith("```json")) {
            response = response.substring(7);
        }

        if (response.startsWith("```")) {
            response = response.substring(3);
        }

        if (response.endsWith("```")) {
            response = response.substring(
                    0,
                    response.length() - 3
            );
        }

        return response.trim();
    }
}