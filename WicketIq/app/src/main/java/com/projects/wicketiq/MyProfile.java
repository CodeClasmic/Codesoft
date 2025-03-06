package com.projects.wicketiq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText userName;
    private TextView email, totalScore;
    private Button editProfileButton;
    private List<String> attemptedQuestions;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        attemptedQuestions = new ArrayList<>();

        // UI Elements
        Button logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        userName = findViewById(R.id.usernameEditText);
        email = findViewById(R.id.emailTextView);
        totalScore = findViewById(R.id.totalScoreEditText);

        userName.setEnabled(false);
        editProfileButton.setText("Edit Profile");

        // Logout function
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MyProfile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyProfile.this, Splash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Edit Profile Click
        editProfileButton.setOnClickListener(view -> {
            if (editProfileButton.getText().toString().equals("Edit Profile")) {
                enterEditMode();
            } else {
                saveProfileChanges();
            }
        });

        // Load user data
        loadUserData();
    }

    // Switch to Edit Mode
    private void enterEditMode() {
        userName.setEnabled(true);
        editProfileButton.setText("Save");
    }

    // Save Profile Changes
    private void saveProfileChanges() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String newUsername = userName.getText().toString().trim();
            DocumentReference userRef = db.collection("users").document(userId);

            if (newUsername.isEmpty()) {
                Toast.makeText(MyProfile.this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    long existingTotalScore = documentSnapshot.contains("totalScore")
                            ? documentSnapshot.getLong("totalScore")
                            : 0;

                    List<String> existingAttemptedQuestions = documentSnapshot.contains("attemptedQuestions")
                            ? (List<String>) documentSnapshot.get("attemptedQuestions")
                            : new ArrayList<>();

                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("username", newUsername);
                    updatedData.put("email", user.getEmail());
                    updatedData.put("totalScore", existingTotalScore);
                    updatedData.put("attemptedQuestions", existingAttemptedQuestions);

                    userRef.update(updatedData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MyProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                userName.setEnabled(false);
                                editProfileButton.setText("Edit Profile");
                            })
                            .addOnFailureListener(e -> Toast.makeText(MyProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    // Load User Data from Firestore
    @SuppressLint("SetTextI18n")
    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String emailId = user.getEmail();

            email.setText(emailId != null ? emailId : "No Email");

            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        long score = documentSnapshot.contains("totalScore")
                                ? documentSnapshot.getLong("totalScore")
                                : 0;
                        attemptedQuestions = documentSnapshot.contains("attemptedQuestions")
                                ? (List<String>) documentSnapshot.get("attemptedQuestions")
                                : new ArrayList<>();

                        userName.setText(username != null ? username : "No Username");
                        totalScore.setText("Total Score: " + score);
                    } else {
                        // If the document does not exist, create it with default values
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("username", "New User");
                        newUser.put("email", emailId);
                        newUser.put("totalScore", 0);
                        newUser.put("attemptedQuestions", new ArrayList<>());

                        userRef.set(newUser)
                                .addOnSuccessListener(aVoid -> {
                                    userName.setText("New User");
                                    totalScore.setText("Total Score: 0");
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create profile", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
