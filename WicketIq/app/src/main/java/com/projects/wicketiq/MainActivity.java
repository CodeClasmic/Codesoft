package com.projects.wicketiq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private boolean hasUserProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout home = findViewById(R.id.home);
        LinearLayout profile = findViewById(R.id.profile);
        LinearLayout scorecard = findViewById(R.id.scorecard);

        CardView slide1 = findViewById(R.id.card1);
        CardView slide2 = findViewById(R.id.card2);
        CardView slide3 = findViewById(R.id.card3);
        CardView slide4 = findViewById(R.id.card4);
        CardView slide5 = findViewById(R.id.card5);
        CardView slide6 = findViewById(R.id.card6);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            checkUserProfile(user.getUid(), home, scorecard, slide1, slide2, slide3, slide4, slide5, slide6);
        }

        cardutility.setCardHover(slide1, MainActivity.this, "HIT THE LEAGUE");
        cardutility.setCardHover(slide2, MainActivity.this, "KNOW YOUR LEGENDS");
        cardutility.setCardHover(slide3, MainActivity.this, "THE ULTIMATE TEST");
        cardutility.setCardHover(slide4, MainActivity.this, "RAPID CHALLENGE");
        cardutility.setCardHover(slide5, MainActivity.this, "T-TWENTY");
        cardutility.setCardHover(slide6, MainActivity.this, "ODI CHALLENGE");

        profile.setOnClickListener(view -> {
            enableInteractions(home, scorecard, slide1, slide2, slide3, slide4, slide5, slide6);
            startActivity(new Intent(MainActivity.this, MyProfile.class));
        });

        scorecard.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LeaderBoard.class)));
    }

    private void checkUserProfile(String userId, LinearLayout home, LinearLayout scorecard, CardView... cards) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    hasUserProfile = true;
                } else {
                    hasUserProfile = false;
                    disableInteractions(home, scorecard, cards);
                    Toast.makeText(this, "Please complete your profile by clicking on the Profile button", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void disableInteractions(LinearLayout home, LinearLayout scorecard, CardView... cards) {
        home.setEnabled(false);
        scorecard.setEnabled(false);
        for (CardView card : cards) {
            card.setEnabled(false);
        }
    }
    private void enableInteractions(LinearLayout home, LinearLayout scorecard, CardView... cards) {
        home.setEnabled(true);
        scorecard.setEnabled(true);
        for (CardView card : cards) {
            card.setEnabled(true);
        }
    }

}
