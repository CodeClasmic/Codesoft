package com.projects.wicketiq;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScoreUpdater {
    private final FirebaseFirestore db;
    private  String userId;

    public ScoreUpdater() {
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }

    public void updateTotalScore(int correctAnswers) {
        if (userId == null) {
            Log.e("ScoreUpdater", "User ID is null, cannot update score");
            return;
        }

        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Number currentScore = documentSnapshot.getDouble("totalScore");
                int newScore = (currentScore != null ? currentScore.intValue() : 0) + correctAnswers;

                userRef.update("totalScore", newScore)
                        .addOnSuccessListener(aVoid -> Log.d("ScoreUpdater", "Score updated successfully"))
                        .addOnFailureListener(e -> Log.e("ScoreUpdater", "Failed to update score", e));
            } else {
                Log.e("ScoreUpdater", "User document does not exist");
            }
        }).addOnFailureListener(e -> Log.e("ScoreUpdater", "Failed to fetch user data", e));
    }







}
