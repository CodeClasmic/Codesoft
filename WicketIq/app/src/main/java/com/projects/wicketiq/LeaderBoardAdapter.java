package com.projects.wicketiq;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardViewHolder> {
    private List<userModel> userlist;

    public LeaderBoardAdapter(List<userModel> userlist)
    {
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_items,parent,false);
        return new LeaderBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {
    userModel user=userlist.get(position);

    holder.UserName.setText(user.getEmail());
    holder.UserScore.setText(String.valueOf(user.getTotalScore()));
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
}
