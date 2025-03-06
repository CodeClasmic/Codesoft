package com.projects.wicketiq;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionTracker {
    private static QuestionTracker instance;
    private final Set<String> attemptedQuestions;
    private final FirebaseFirestore db;
    private final String userId;
    private boolean dataLoaded = false;
    private FirestoreDataListener firestoreDataListener;

    public interface FirestoreDataListener {
        void onDataLoaded();
    }

    private QuestionTracker() {
        attemptedQuestions = new HashSet<>();
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        fetchAttemptedQuestions();
    }

    public static synchronized QuestionTracker getInstance() {
        if (instance == null) {
            instance = new QuestionTracker();
        }
        return instance;
    }

    public void setFirestoreDataListener(FirestoreDataListener listener) {
        this.firestoreDataListener = listener;
        if (dataLoaded && firestoreDataListener != null) {
            firestoreDataListener.onDataLoaded();
        }
    }

    private void fetchAttemptedQuestions() {
        if (userId == null) return;

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("attemptedQuestions")) {
                        Object questionsObj = documentSnapshot.get("attemptedQuestions");
                        if (questionsObj instanceof List<?>) {
                            for (Object obj : (List<?>) questionsObj) {
                                if (obj instanceof String) {
                                    attemptedQuestions.add((String) obj);
                                }
                            }
                        }
                        Log.d("Firestore", "Fetched attemptedQuestions: " + attemptedQuestions);
                    }
                    dataLoaded = true;
                    if (firestoreDataListener != null) {
                        firestoreDataListener.onDataLoaded();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to fetch attemptedQuestions", e));
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void markQuestionAsAttempted(String questionText) {
        if (questionText != null && !questionText.trim().isEmpty()) {
            attemptedQuestions.add(questionText);
            updateFirestore(questionText);
        }
    }

    public Set<String> getAttemptedQuestions() {
        return new HashSet<>(attemptedQuestions);
    }

    private void updateFirestore(String questionText) {
        if (userId == null || questionText == null || questionText.trim().isEmpty()) return;

        DocumentReference userDoc = db.collection("users").document(userId);

        userDoc.update("attemptedQuestions", FieldValue.arrayUnion(questionText))
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Question added to attemptedQuestions"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to update attemptedQuestions", e));
    }
}
