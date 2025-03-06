package com.projects.wicketiq;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private FirebaseFirestore firestore;
    private List<QuestionModel> questionList = new ArrayList<>();

    public Question() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void loadQuestion(Context context, QuizActivity activity, String quizCategory) {
        firestore.collection("Categories").document(quizCategory).collection("questions").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        questionList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            QuestionModel question = document.toObject(QuestionModel.class);
                            questionList.add(question);
                        }
                        if (!questionList.isEmpty()) {
                            activity.displayQuestion(0, questionList); // Direct call to QuizActivity
                        } else {
                            Toast.makeText(context, "No questions found for " + quizCategory, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                        Log.e("QuizActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}