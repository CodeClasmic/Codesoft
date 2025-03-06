package com.projects.wicketiq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    private AuthManager authManager;
    Button loginButton,registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.spl), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);

        loginButton.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);

        authManager=new AuthManager();

        new Handler(Looper.getMainLooper()).postDelayed(() ->{
            FirebaseUser currentUser= authManager.getCurrentUser();
            if (currentUser!=null)
            {
                startActivity(new Intent(Splash.this,MainActivity.class));
                finish();
            }else
            {
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);

            }
        },2000);

        // Login Button
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(Splash.this, LoginActivity.class));
            finish();
        });

        // Register Button

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(Splash.this, RegisterActivity.class));
            finish();
        });




    }
}
