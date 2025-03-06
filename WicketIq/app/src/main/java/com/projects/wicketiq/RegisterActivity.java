package com.projects.wicketiq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authManager = new AuthManager();

        EditText emailField = findViewById(R.id.etEmail);
        EditText passwordField = findViewById(R.id.etPassword);
        Button registerButton = findViewById(R.id.btnRegister);


        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, password);
            }
        });


    }

    private void registerUser(String email, String password) {
        authManager.registerUser(email, password, this, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Toast.makeText(RegisterActivity.this, "Verification Email sent to your email", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RegisterActivity.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
