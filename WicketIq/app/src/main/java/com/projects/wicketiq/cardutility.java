package com.projects.wicketiq;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class cardutility {

    public static void setCardHover(@NonNull CardView cardView, @NonNull Context context, @NonNull String category) {
        cardView.setOnClickListener(v -> {
            cardView.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        // Scale back to original size after scaling up
                        cardView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(300)
                                .start();
                        cardView.setCardElevation(8f);
                    })
                    .start();
            cardView.setCardElevation(16f);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("quiz_category", category);
                context.startActivity(intent);
            }, 200);
        });
    }
}
