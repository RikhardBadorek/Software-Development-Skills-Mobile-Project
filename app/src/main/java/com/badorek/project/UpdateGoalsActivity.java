package com.badorek.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateGoalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_goals);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        displayCurrentGoals();
    }

    @SuppressLint("SetTextI18n")
    private void displayCurrentGoals() {
        Goal currentGoal = GoalManager.getInstance().getGoal();

        TextView currentGoals = findViewById(R.id.TVcurrentGoals);

        if (currentGoal == null) {
            currentGoals.setText("You have not set any goals yet.");
        } else {
            int workoutGoal = currentGoal.workoutGoal;
            int waterGoal = currentGoal.waterGoal;
            int sleepGoal = currentGoal.sleepGoal;

            currentGoals.setText("Your current goals are:\n"
                    + "Workout: " + workoutGoal + " workouts a week\n"
                    + "Water: " + waterGoal + " liters a day\n"
                    + "Sleep: " + sleepGoal + " hours a night");
        }
    }
    public void addGoals(View view) {
        EditText workoutGoal = findViewById(R.id.editTextWorkoutGoal);
        EditText waterGoal = findViewById(R.id.editTextWaterGoal);
        EditText sleepGoal = findViewById(R.id.editTextSleepGoal);

        int goal1 = !workoutGoal.getText().toString().isEmpty() ? Integer.parseInt(workoutGoal.getText().toString()) : 0;
        int goal2 = !waterGoal.getText().toString().isEmpty() ? Integer.parseInt(waterGoal.getText().toString()) : 0;
        int goal3 = !sleepGoal.getText().toString().isEmpty() ? Integer.parseInt(sleepGoal.getText().toString()) : 0;

        Goal newGoal = new Goal(goal1, goal2, goal3);

        GoalManager.getInstance().setGoal(newGoal);
        GoalManager.getInstance().saveGoal(this);

        switchToMain();

    }

    public void switchToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}