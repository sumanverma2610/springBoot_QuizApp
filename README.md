# AI-Powered Quiz Management System

A role-based Quiz Management System built with **Spring Boot**, **MySQL**, **Spring Security**, **Thymeleaf**, and **Gemini AI**. The application provides separate functionality for Admin and Student users, and supports AI-powered quiz question generation.

## 🚀 Features

### 👨‍💼 Admin
- Secure Admin authentication with role-based authorization
- Add, view, and delete quiz questions
- Manage quiz categories
- View all students' quiz results and dashboard stats
- Generate quiz questions automatically using Gemini AI
- AI Test page for trying custom prompts

### 👨‍🎓 Student
- Secure Student registration and login
- Browse categories and attempt quizzes
- Automatic quiz evaluation and score calculation
- View current and previous quiz results

### 🤖 Gemini AI Integration
- Generate multiple-choice questions automatically for a chosen topic
- Automatically save generated questions to the database, tagged with a category

## 🛠️ Technologies Used

- Java 25
- Spring Boot 4.1 (Web MVC, Security, Data JPA, Validation)
- Thymeleaf
- MySQL
- Gemini AI API
- Maven

## 📂 Project Structure

```
src/main/java/com/Quiz/QuizApplication
├── config/          # Security config, password encoder, RestTemplate bean
├── controller/       # MVC controllers (Auth, Admin, Quiz, Category, AI, Home)
├── dto/              # Gemini request/response and AI question DTOs
├── entity/            # JPA entities: User, Question, Category, Result
├── repository/       # Spring Data JPA repositories
├── service/           # Business logic (users, questions, categories, results, Gemini)
└── QuizApplication.java

src/main/resources
├── application.properties
├── templates/          # Thymeleaf views
└── static/
```

## ⚙️ Getting Started

### Prerequisites
- Java 25 (JDK)
- Maven (or use the bundled `./mvnw`)
- MySQL Server running locally or reachable remotely
- A Gemini API key ([Google AI Studio](https://aistudio.google.com/))

### 1. Create the database

```sql
CREATE DATABASE quiz_app;
```

Tables are created/updated automatically on startup (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Configure environment variables

Database credentials and the Gemini API key are **not** hardcoded — they're read from environment variables at startup, with local-dev defaults for the database:

| Variable | Required | Default | Description |
|---|---|---|---|
| `GEMINI_API_KEY` | ✅ Yes | — | Your Gemini API key |
| `GEMINI_MODEL` | No | `gemini-3.5-flash` | Gemini model used for question generation |
| `DB_URL` | No | `jdbc:mysql://localhost:3306/quiz_app` | JDBC URL for MySQL |
| `DB_USERNAME` | No | `root` | MySQL username |
| `DB_PASSWORD` | No | *(empty)* | MySQL password |

Set them before running, e.g. on macOS/Linux:

```bash
export GEMINI_API_KEY=your_real_key_here
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

Or on Windows (PowerShell):

```powershell
$env:GEMINI_API_KEY="your_real_key_here"
$env:DB_PASSWORD="your_mysql_password"
```

**Never commit a real API key or password to `application.properties` or to git.**

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The app starts on **http://localhost:8080** by default.

### 4. Create your first Admin account

New registrations via `/register` are always created with the `STUDENT` role. To get an Admin account, register normally and then update that user's role directly in the database:

```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

## 🔒 Roles & Access

| Path | Access |
|---|---|
| `/`, `/login`, `/register` | Public |
| `/admin/**` | ADMIN only |
| `/ai/**` | ADMIN or STUDENT |
| `/quiz`, `/quiz/start`, `/submitQuiz`, `/my-results` | STUDENT only |
| Everything else | Any authenticated user |

## 🔒 Security Notes

- Passwords are hashed with BCrypt (`PasswordEncoder` bean).
- Database credentials and the Gemini API key are supplied via environment variables — see the table above. `application.properties` itself should never contain real secrets, and is already listed in `.gitignore`.
- If you ever accidentally commit or share a real API key, rotate it immediately in Google AI Studio.

## 📌 Project Highlights

- Role-based authentication and authorization
- CRUD-based question and category management
- Automatic quiz scoring and result history
- Gemini AI integration for automatic question generation
- MySQL database integration via Spring Data JPA

## 👨‍💻 Author

Suman Verma — MCA Student, Full Stack Developer