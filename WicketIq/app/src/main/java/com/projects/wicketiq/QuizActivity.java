package com.projects.wicketiq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, timerText;
    private Button option1, option2, option3, option4, nextButton, checkButton;
    private List<QuestionModel> questionList;
    private int currentIndex = 0;
    private String selectedAnswer = "", answer = "";
    private CountDownTimer timer;
    private int correctAnswer = 0, totalQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });

        String quizCategory = getIntent().getStringExtra("quiz_category");
        TextView category = findViewById(R.id.categoryTitle);
        timerText = findViewById(R.id.timerText);

        category.setText(quizCategory);
        assert quizCategory != null;
        quizCategory = quizCategory.trim();

      //  Question questionLoader = new Question();
        //questionLoader.loadQuestion(this, this, quizCategory);

        questionText = findViewById(R.id.questionText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        checkButton = findViewById(R.id.checkButton);

        checkButton.setEnabled(false);
        nextButton.setEnabled(false);

        option1.setOnClickListener(v -> SelectedOption(option1));
        option2.setOnClickListener(v -> SelectedOption(option2));
        option3.setOnClickListener(v -> SelectedOption(option3));
        option4.setOnClickListener(v -> SelectedOption(option4));

        checkButton.setOnClickListener(view -> {
            checkAnswer();
            stopTimer();
        });

        nextButton.setOnClickListener(v -> {
            stopTimer();
            currentIndex++;

            while (currentIndex < questionList.size() &&
                    QuestionTracker.getInstance().getAttemptedQuestions().contains(questionList.get(currentIndex).getQuestion())) {
                currentIndex++;
            }

            if (currentIndex < questionList.size()) {
                displayQuestion(currentIndex, questionList);
            } else {
                Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
                QuizCompleteDialog scoreDialog = new QuizCompleteDialog(this);
                scoreDialog.showDialog(correctAnswer, totalQuestion);
                QuizScoreUpdate();
            }
        });

        // Load questions only after attempted ones are fetched
        String finalQuizCategory = quizCategory;
        QuestionTracker.getInstance().setFirestoreDataListener(() -> {
            Question questionLoader = new Question();
            questionLoader.loadQuestion(this, this, finalQuizCategory);
        });
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Quiz")
                .setMessage("Are you sure you want to exit the quiz?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    QuizScoreUpdate();
                    stopTimer();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void displayQuestion(int index, @NonNull List<QuestionModel> questionList) {
        this.questionList = questionList;

        if (!QuestionTracker.getInstance().isDataLoaded()) {
            //Toast.makeText(this, "Loading previous attempts...", Toast.LENGTH_SHORT).show();
            return;
        }

        while (index < questionList.size()) {
            QuestionModel q = questionList.get(index);

            if (!QuestionTracker.getInstance().getAttemptedQuestions().contains(q.getQuestion())) {
                totalQuestion++;
                questionText.setText(q.getQuestion());
                option1.setText(q.getOption1());
                option2.setText(q.getOption2());
                option3.setText(q.getOption3());
                option4.setText(q.getOption4());
                answer = q.getAnswer();
                nextButton.setEnabled(false);
                resetOptions();
                startTimer();
                return;
            }
            index++;
        }

        Toast.makeText(this, "No new questions available.", Toast.LENGTH_SHORT).show();
        disableAllOptions();
    }

    public void SelectedOption(Button selectedButton) {
        resetOptions();
        checkButton.setEnabled(true);
        selectedButton.setEnabled(false);
        selectedButton.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        selectedButton.setScaleX(1.1F);
        selectedButton.setScaleY(1.1F);
        selectedAnswer = selectedButton.getText().toString();
    }

    private void resetOptions() {
        Button[] options = {option1, option2, option3, option4};
        for (Button option : options) {
            option.setBackgroundColor(Color.parseColor("#6175F3"));
            option.setScaleX(1F);
            option.setScaleY(1F);
            option.setEnabled(true);
        }
        selectedAnswer = "";
        checkButton.setEnabled(false);
    }

    private void checkAnswer() {
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Please select an answer first!", Toast.LENGTH_SHORT).show();
            return;
        }

        Button[] options = {option1, option2, option3, option4};

        // Disable all options and highlight the correct answer
        for (Button option : options) {
            option.setEnabled(false);
            if (option.getText().toString().equals(answer)) {
                option.setBackgroundColor(Color.parseColor("#4CAF50")); // Green for correct answer
            }
        }

        // Highlight the user's selected option
        if (!selectedAnswer.equals(answer)) {
            for (Button option : options) {
                if (option.getText().toString().equals(selectedAnswer)) {
                    option.setBackgroundColor(Color.parseColor("#F44336")); // Red for wrong answer
                    break;
                }
            }
        } else {
            correctAnswer++;
        }

        // Store the attempted question in Firestore
        QuestionTracker.getInstance().markQuestionAsAttempted(questionText.getText().toString());

        Toast.makeText(this, selectedAnswer.equals(answer) ? "✅ Correct Answer!"
                : "❌ Wrong Answer! Correct: " + answer, Toast.LENGTH_SHORT).show();

        nextButton.setEnabled(true);
        checkButton.setEnabled(false);
    }


    @SuppressLint("SetTextI18n")
    private void startTimer() {
        timerText.setText("Time: 10s");
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(10000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish()
            {
                Toast.makeText(QuizActivity.this, "⏰ Time's up!", Toast.LENGTH_SHORT).show();
                QuestionTracker.getInstance().markQuestionAsAttempted(questionText.getText().toString());
                disableAllOptions();

                if (currentIndex < questionList.size() - 1)
                {
                    currentIndex++;
                    displayQuestion(currentIndex, questionList);
                } else
                {
                    QuizCompleteDialog scoreDialog = new QuizCompleteDialog(QuizActivity.this);
                    scoreDialog.showDialog(correctAnswer, totalQuestion);
                }
            }
        }.start();
    }

    private void disableAllOptions() {
        Button[] options = {option1, option2, option3, option4};
        for (Button option : options) {
            option.setEnabled(false);
        }
        checkButton.setEnabled(false);
        nextButton.setEnabled(false);
    }


    private void stopTimer() {
        if (timer != null) timer.cancel();
    }

    private void QuizScoreUpdate()
    {
        ScoreUpdater scoreUpdater=new ScoreUpdater();
        scoreUpdater.updateTotalScore(correctAnswer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
