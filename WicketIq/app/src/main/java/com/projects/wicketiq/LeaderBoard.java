package com.projects.wicketiq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderBoard extends AppCompatActivity {
    private TextView topUserName, topUserScore;
    private RecyclerView recyclerView;
    private List<userModel> userModelList;
    private LeaderBoardAdapter adapter;
    private FirebaseFirestore db;
    private ListenerRegistration leaderboardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leader_board);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.leadactivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Top User Card Elements
        topUserName = findViewById(R.id.top_user_name);
        topUserScore = findViewById(R.id.top_user_score);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userModelList = new ArrayList<>();
        adapter = new LeaderBoardAdapter(userModelList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadLeaderBoard();
    }

    @SuppressLint("SetTextI18n")
    private void loadLeaderBoard() {
        CollectionReference usersRef = db.collection("users");

        leaderboardListener = usersRef.orderBy("totalScore", Query.Direction.DESCENDING).limit(20)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        if (error.getCode() == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                            Toast.makeText(this, "Please log in to view the leaderboard", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error loading LeaderBoard: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    if (value == null || value.isEmpty()) {
                        Toast.makeText(this, "No leaderboard data found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userModelList.clear(); // Clear old data
                    int index = 0;

                    for (QueryDocumentSnapshot document : value) {
                        if (!document.contains("totalScore") || !document.contains("email")) continue;

                        String email = document.getString("email");
                        int score = Objects.requireNonNull(document.getLong("totalScore")).intValue();
                        userModel user = new userModel(email, score);

                        if (index == 0) {
                            // Set the highest scorer in the top card
                            topUserName.setText(email);
                            topUserScore.setText("Total Score: " + score);
                        } else {
                            userModelList.add(user);
                        }
                        index++;
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    // Stop listening when activity is no longer visible to prevent errors on logout
    @Override
    protected void onStop() {
        super.onStop();
        if (leaderboardListener != null) {
            leaderboardListener.remove();
            leaderboardListener = null;
        }
    }
}
