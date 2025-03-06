package com.projects.wicketiq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private AuthManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailField = findViewById(R.id.etEmail);
        EditText passwordField = findViewById(R.id.etPassword);
        Button loginButton = findViewById(R.id.btnLogin);

        authManager=new AuthManager();


        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
                loginUser(email, password);
            }
        });


    }

    private void loginUser(String email, String password) {
        authManager.loginUser(email, password, this, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
