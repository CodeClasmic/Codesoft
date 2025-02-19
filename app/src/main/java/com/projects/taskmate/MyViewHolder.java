package com.projects.taskmate;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CheckBox cbTaskDone;
    TextView tvTaskTitle, tvTaskDescription;
    ImageButton btnDeleteTask;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        cbTaskDone = itemView.findViewById(R.id.cbTaskDone);
        tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
        tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
        btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
    }
}

