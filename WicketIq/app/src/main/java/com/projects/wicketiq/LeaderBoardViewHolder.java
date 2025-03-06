package com.projects.wicketiq;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
    ImageView DpImage;
    TextView UserName, UserScore;
    public LeaderBoardViewHolder(@NonNull View itemView) {
        super(itemView);
        DpImage = itemView.findViewById(R.id.profileImageView);
        UserName = itemView.findViewById(R.id.usernameTextView);
        UserScore = itemView.findViewById(R.id.scoreTextView);
    }
}
