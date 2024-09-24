package com.badorek.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.Calendar;

public class DailyLogActivity extends AppCompatActivity {

    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dailylog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CalendarView calendarView = findViewById(R.id.calendarViewDailyLog);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        if (selectedDate == null) {
            Calendar today = Calendar.getInstance();
            selectedDate = String.format("%04d-%02d-%02d", today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        }
    }
    public void saveLog(View view) {
        EditText workoutsCompleted = findViewById(R.id.editTextDailyWorkouts);
        EditText waterIntake = findViewById(R.id.editTextDailyWaterIntake);
        EditText sleepHours = findViewById(R.id.editTextSleepHours);

        int workouts = !workoutsCompleted.getText().toString().isEmpty() ? Integer.parseInt(workoutsCompleted.getText().toString()) : 0;
        float water = !waterIntake.getText().toString().isEmpty() ? Float.parseFloat(waterIntake.getText().toString()) : 0.0f;
        float sleep = !sleepHours.getText().toString().isEmpty() ? Float.parseFloat(sleepHours.getText().toString()) : 0.0f;

        DailyLog newLog = new DailyLog(workouts, water, sleep, selectedDate);

        if (DailyLogManager.getInstance().logExists(selectedDate)) {
            new AlertDialog.Builder(this)
                    .setMessage("A log for today already exists. Would you like to overwrite it?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DailyLogManager.getInstance().addLog(newLog);
                        DailyLogManager.getInstance().saveLogs(this);
                        switchToMain();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        } else {
            DailyLogManager.getInstance().addLog(newLog);
            DailyLogManager.getInstance().saveLogs(this);
            switchToMain();
        }
    }
    public void switchToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}