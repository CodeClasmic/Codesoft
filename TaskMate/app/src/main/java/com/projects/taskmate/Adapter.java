package com.projects.taskmate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<MyViewHolder> {
    private final List<task> taskList;

    public Adapter(List<task> taskList) {
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        task task = taskList.get(position);

        holder.tvTaskTitle.setText(task.getTitle());
        holder.tvTaskDescription.setText(task.getDescription());
        holder.cbTaskDone.setOnCheckedChangeListener(null);
        holder.cbTaskDone.setChecked(task.isDone());



        //  Checkbox Click Listener
        holder.cbTaskDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            notifyItemChanged(position);
            TaskStorage.saveTasks(holder.itemView.getContext(),taskList);
        });

        if (task.isHighPriority()) {
            holder.tvTaskTitle.setTextColor(Color.RED);
        }
        else{
            holder.tvTaskTitle.setTextColor(Color.parseColor("#1565C0"));
        }

        // Delete Button Click Listener
        holder.btnDeleteTask.setOnClickListener(v -> {
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());

            TaskStorage.saveTasks(holder.itemView.getContext(),taskList);
        });

        holder.tvTaskTitle.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());

            // Inflate the dialog layout
            View dialogView = inflater.inflate(R.layout.dialog_task_details, null);
            builder.setView(dialogView);


            EditText etTaskTitle = dialogView.findViewById(R.id.tvTaskTitle);
            EditText etTaskDescription = dialogView.findViewById(R.id.tvTaskDescription);
            Button btnClose = dialogView.findViewById(R.id.btnClose);
            ImageButton btnEditTask = dialogView.findViewById(R.id.btnEditTask);
            TextView tvTaskDueDate = dialogView.findViewById(R.id.tvTaskDueDate);
            tvTaskDueDate.setText(task.getDueDate());



            etTaskTitle.setText(task.getTitle());
            etTaskDescription.setText(task.getDescription());


            etTaskTitle.setEnabled(false);
            etTaskDescription.setEnabled(false);


            AlertDialog dialog = builder.create();
            dialog.show();

            final boolean[] isEditing = {false};


            btnEditTask.setOnClickListener(view -> {
                if (!isEditing[0]) {

                    etTaskTitle.setEnabled(true);
                    etTaskDescription.setEnabled(true);
                    etTaskTitle.requestFocus();
                    btnEditTask.setImageResource(android.R.drawable.ic_menu_save);
                } else {

                    task.setTitle(etTaskTitle.getText().toString().trim());
                    task.setDescription(etTaskDescription.getText().toString().trim());
                    notifyItemChanged(holder.getAdapterPosition()); // Update RecyclerView
                    TaskStorage.saveTasks(holder.itemView.getContext(), taskList);


                    etTaskTitle.setEnabled(false);
                    etTaskDescription.setEnabled(false);
                    btnEditTask.setImageResource(android.R.drawable.ic_menu_edit); // Change back to Edit
                }
                isEditing[0] = !isEditing[0]; // Toggle edit mode
            });

            // Close button click - Dismiss dialog
            btnClose.setOnClickListener(view -> dialog.dismiss());

            return true;
        });

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }


}
