package com.badorek.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadGoals();
        loadLogs();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void loadLogs() {
        DailyLogManager.getInstance().loadLogs(this);
    }

    public void loadGoals() {
        GoalManager.getInstance().loadGoal(this);
    }

    public void switchToGoals(View view) {
        Intent intent = new Intent(this, UpdateGoalsActivity.class);
        startActivity(intent);
    }

    public void switchToDailyLog(View view) {
        Intent intent = new Intent(this, DailyLogActivity.class);
        startActivity(intent);
    }

    //https://developer.android.com/training/basics/intents/result
    //Reference
    public void switchToProgress(View view) {
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void switchToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    //https://stackoverflow.com/questions/20114485/use-onactivityresult-android
    //Reference
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            float waterAverage = data.hasExtra("waterAverage") ? data.getFloatExtra("waterAverage", 0.0f) : 0.0f;
            float sleepAverage = data.hasExtra("sleepAverage") ? data.getFloatExtra("sleepAverage", 0.0f) : 0.0f;
            int weeksWorkouts = data.hasExtra("weeksWorkouts") ? data.getIntExtra("weeksWorkouts", 0) : 0;

            TextView quote = findViewById(R.id.textViewMainPageQuote);

            StringBuilder fullQuote = new StringBuilder();

            Goal currentGoal = GoalManager.getInstance().getGoal();
            if (currentGoal != null) {
                if (waterAverage < currentGoal.getWaterGoal() * 0.6) {
                    fullQuote.append("Keep drinking more water, you'll feel better!!\n");
                }
                if (sleepAverage < currentGoal.getSleepGoal() * 0.6) {
                    fullQuote.append("Make sure to get some quality sleep!!\n");
                }
                if (weeksWorkouts < currentGoal.getWorkoutGoal()) {
                    fullQuote.append("Keep working out, still some work to do for the week!!\n");
                }

                if (fullQuote.length() > 0) {
                    quote.setText(fullQuote.toString());
                } else {
                    quote.setText("Amazing job! Stay on track with your goals!");
                }
            }else {
                quote.setText("Keep track of your progress and get after it!!");
            }
        }
    }
}