package com.example.quizapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // --- Quiz Data ---
    private final String[] questions = {
            "What does CPU stand for?",
            "Which data structure uses LIFO order?",
            "What is the time complexity of binary search?",
            "Which language is primarily used for Android development?",
            "What does HTML stand for?",
            "Which sorting algorithm has the best average time complexity?",
            "What is an IP address?",
            "Which of these is NOT a programming language?",
            "What does RAM stand for?",
            "What symbol is used for single-line comments in Java?"
    };

    private final String[][] options = {
            {"Central Processing Unit", "Computer Personal Unit", "Central Program Utility", "Core Processing Unit"},
            {"Queue", "Stack", "Array", "Tree"},
            {"O(n)", "O(n²)", "O(log n)", "O(1)"},
            {"Swift", "Kotlin / Java", "Python", "Ruby"},
            {"HyperText Markup Language", "High Transfer Markup Language", "HyperText Machine Language", "Home Tool Markup Language"},
            {"Bubble Sort", "Merge Sort", "Selection Sort", "Insertion Sort"},
            {"A unique identifier for a device on a network", "A website address", "A type of computer virus", "A programming term"},
            {"Python", "Java", "Hyper++", "JavaScript"},
            {"Random Access Memory", "Read Allocate Memory", "Runtime Application Memory", "Remote Access Module"},
            {"//", "##", "**", "//!"}
    };

    private final int[] correctAnswers = {0, 1, 2, 1, 0, 1, 0, 2, 0, 0};

    // --- State ---
    private int currentQuestion = 0;
    private int score = 0;
    private boolean answered = false;

    // --- UI ---
    private TextView tvQuestion, tvProgress, tvScore, tvResult;
    private Button[] optionButtons = new Button[4];
    private Button btnNext;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion  = findViewById(R.id.tvQuestion);
        tvProgress  = findViewById(R.id.tvProgress);
        tvScore     = findViewById(R.id.tvScore);
        tvResult    = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        btnNext     = findViewById(R.id.btnNext);

        optionButtons[0] = findViewById(R.id.btnOption0);
        optionButtons[1] = findViewById(R.id.btnOption1);
        optionButtons[2] = findViewById(R.id.btnOption2);
        optionButtons[3] = findViewById(R.id.btnOption3);

        progressBar.setMax(questions.length);

        for (int i = 0; i < 4; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(v -> handleAnswer(index));
        }

        btnNext.setOnClickListener(v -> {
            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                showResults();
            }
        });

        loadQuestion();
    }

    private void loadQuestion() {
        answered = false;
        tvResult.setText("");
        btnNext.setVisibility(View.GONE);

        tvProgress.setText("Question " + (currentQuestion + 1) + " of " + questions.length);
        tvScore.setText("Score: " + score);
        progressBar.setProgress(currentQuestion);
        tvQuestion.setText(questions[currentQuestion]);

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[currentQuestion][i]);
            optionButtons[i].setEnabled(true);
            optionButtons[i].setBackgroundColor(Color.parseColor("#2C3E50"));
            optionButtons[i].setTextColor(Color.WHITE);
        }
    }

    private void handleAnswer(int selected) {
        if (answered) return;
        answered = true;

        int correct = correctAnswers[currentQuestion];

        for (Button b : optionButtons) b.setEnabled(false);

        if (selected == correct) {
            score++;
            optionButtons[selected].setBackgroundColor(Color.parseColor("#27AE60"));
            tvResult.setText("Correct!");
            tvResult.setTextColor(Color.parseColor("#27AE60"));
        } else {
            optionButtons[selected].setBackgroundColor(Color.parseColor("#E74C3C"));
            optionButtons[correct].setBackgroundColor(Color.parseColor("#27AE60"));
            tvResult.setText("Wrong! Correct answer: " + options[currentQuestion][correct]);
            tvResult.setTextColor(Color.parseColor("#E74C3C"));
        }

        tvScore.setText("Score: " + score);

        new Handler().postDelayed(() -> btnNext.setVisibility(View.VISIBLE), 800);
    }

    private void showResults() {
        tvQuestion.setText("Quiz Complete!");
        tvResult.setText("");
        for (Button b : optionButtons) b.setVisibility(View.GONE);
        btnNext.setText("Restart");
        btnNext.setVisibility(View.VISIBLE);
        progressBar.setProgress(questions.length);

        String grade;
        if      (score == questions.length)      grade = "Perfect Score!";
        else if (score >= questions.length * 0.8) grade = "Excellent!";
        else if (score >= questions.length * 0.6) grade = "Good job!";
        else if (score >= questions.length * 0.4) grade = "Keep practicing!";
        else                                       grade = "Better luck next time!";

        tvProgress.setText(grade);
        tvScore.setText("Final Score: " + score + " / " + questions.length);

        btnNext.setOnClickListener(v -> restartQuiz());
    }

    private void restartQuiz() {
        currentQuestion = 0;
        score = 0;
        btnNext.setText("Next");
        for (Button b : optionButtons) b.setVisibility(View.VISIBLE);
        loadQuestion();
    }
}
