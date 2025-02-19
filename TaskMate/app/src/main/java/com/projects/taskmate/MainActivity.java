package com.projects.taskmate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter taskAdapter;
    private List<task> taskList;

    private ConstraintLayout mainLayout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 🟢 Apply window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



// 🟢 Set Adapter
        taskList=TaskStorage.loadTasks(this);
        taskAdapter = new Adapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchDarkMode = findViewById(R.id.switch1);
        mainLayout = findViewById(R.id.main);
        textView = findViewById(R.id.textView);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mainLayout.setBackgroundColor(Color.parseColor("#3E2723")); // Set background to black
               // textView.setTextColor(Color.WHITE); // Change text color to white
            } else {
                mainLayout.setBackgroundColor(Color.parseColor("#FFF5E1")); // Set original background
              //  textView.setTextColor(Color.BLACK);
            }
        });



        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // Create the AlertDialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Inflate the custom dialog layout
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_task, null);

            // Set the custom view for the dialog
            builder.setView(dialogView);


            EditText etTaskTitle = dialogView.findViewById(R.id.etTaskTitle);
            EditText etTaskDescription = dialogView.findViewById(R.id.etTaskDescription);
            Button btnAddTask = dialogView.findViewById(R.id.btnAddTask);
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchPriority = dialogView.findViewById(R.id.switchPriority);
         //   task.setHighPriority(switchPriority.isChecked());
            TextView tvDueDateTime=dialogView.findViewById(R.id.tvDueDateTime);

            switchPriority.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Toast.makeText(dialogView.getContext(), "Priority cannot be changed later", Toast.LENGTH_SHORT).show();
                }
            });



            AlertDialog dialog = builder.create();
            dialog.show();


            btnAddTask.setOnClickListener(view -> {
                String title = etTaskTitle.getText().toString().trim();
                String description = etTaskDescription.getText().toString().trim();
                boolean isHighPriority = switchPriority.isChecked();
                String date=tvDueDateTime.getText().toString().trim();

                if (!title.isEmpty()) {

                    task newTask = new task(title, description, false, isHighPriority,date);
                    if (isHighPriority) {
                        taskList.add(0, newTask); // Add at top
                        taskAdapter.notifyItemInserted(0);

                    } else {
                        taskList.add(newTask); // Add at bottom
                        taskAdapter.notifyItemInserted(taskList.size() - 1);
                    }
                    TaskStorage.saveTasks(this,taskList);
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            });
            Button btnPickDueDateTime=dialogView.findViewById(R.id.btnPickDueDateTime);
           // TextView tvDueDateTime=dialogView.findViewById(R.id.tvDueDateTime);

            final Calendar calendar=Calendar.getInstance();

            btnPickDueDateTime.setOnClickListener(view -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this,
                        (view1, year, month, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                            new AlertDialog.Builder(this)
                                    .setTitle("Select Time?")
                                    .setMessage("Do you want to set a specific time for this date?")
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {

                                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                                this,
                                                (timeView, hourOfDay, minute) -> {
                                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                    calendar.set(Calendar.MINUTE, minute);

                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                                                    String formattedDateTime = sdf.format(calendar.getTime());
                                                    tvDueDateTime.setText("Due: " + formattedDateTime);
                                                },
                                                calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                false
                                        );
                                        timePickerDialog.show();
                                    })
                                    .setNegativeButton("No", (dialogInterface, i) -> {
                                        // Only date if user chooses 'No'
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                                        String formattedDate = sdf.format(calendar.getTime());
                                        tvDueDateTime.setText("Due: " + formattedDate);
                                    })
                                    .show();
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            });

        });


    }
}