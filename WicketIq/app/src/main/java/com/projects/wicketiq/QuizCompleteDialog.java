package com.projects.wicketiq;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class QuizCompleteDialog {
    private Context context;
    private Dialog dialog;
    private EditText scoreTitle, correctnessPercentage, scoreDisplay;
    private CircularProgressIndicator progressIndicator;
    private Button MainMenu;

    public QuizCompleteDialog(Context context)
    {
        this.context=context;
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.scoresheet);


        scoreTitle=dialog.findViewById(R.id.scoreTitle);
        correctnessPercentage=dialog.findViewById(R.id.correctnessPercentage);
        scoreDisplay=dialog.findViewById(R.id.scoreDisplay);
        progressIndicator=dialog.findViewById(R.id.circularProgressIndicator);
        progressIndicator.setMax(100);
        MainMenu=dialog.findViewById(R.id.mainMenuButton);

        MainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dialog.dismiss();
        });
    }
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void showDialog(int correctAnswers, int totalQuestions) {
        float percentage = ((float) correctAnswers / totalQuestions) * 100; // Ensure decimal division

        scoreDisplay.setText("Your Score: " + correctAnswers + "/" + totalQuestions);
        correctnessPercentage.setText(String.format("%.1f%%", percentage)); // Format to 1 decimal place
        progressIndicator.setProgress((int) percentage); // Convert back to integer for ProgressBar
        dialog.show();
    }


}
