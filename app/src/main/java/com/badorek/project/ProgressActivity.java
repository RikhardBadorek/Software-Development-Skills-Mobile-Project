package com.badorek.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    private ProgressBar dailyWaterProgressBar;
    private ProgressBar dailySleepProgressBar;
    private ProgressBar weeklyWaterProgressBar;
    private ProgressBar weeklySleepProgressBar;

    private TextView dailyWorkoutTextView;
    private TextView weeklyWorkoutTextView;

    private float waterAverage = 0.0f;
    private float sleepAverage = 0.0f;
    private int weeksWorkouts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dailyWaterProgressBar = findViewById(R.id.progressBarWaterIntakeDaily);
        dailySleepProgressBar = findViewById(R.id.progressBarSleepDaily);
        weeklyWaterProgressBar = findViewById(R.id.progressBarWaterIntakeWeekly);
        weeklySleepProgressBar = findViewById(R.id.progressBarSleepWeekly);

        dailyWorkoutTextView = findViewById(R.id.textViewWorkoutProgressionDaily);
        weeklyWorkoutTextView = findViewById(R.id.textViewWorkoutProgressionWeekly);
        
        showDailyProgress();
        showWeeklyProgress();
    }

    private Calendar getStartOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private List<DailyLog> loadLogsForTheWeek() {
        Calendar startOfTheWeek = getStartOfTheWeek();

        List<DailyLog> weeklyLogs = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            String date = String.format("%04d-%02d-%02d", startOfTheWeek.get(Calendar.YEAR),
                    startOfTheWeek.get(Calendar.MONTH) + 1, startOfTheWeek.get(Calendar.DAY_OF_MONTH));

            DailyLog log = DailyLogManager.getInstance().getLogByDate(date);
            if (log != null) {
                weeklyLogs.add(log);
            }
            startOfTheWeek.add(Calendar.DAY_OF_MONTH, 1);
        }

        return weeklyLogs;
    }
    private void showWeeklyProgress() {
        List<DailyLog> weeklyLogs = loadLogsForTheWeek();

        Goal currentGoal = GoalManager.getInstance().getGoal();

        weeksWorkouts = 0;
        float weeksWaterIntake = 0.0f;
        float weeksSleep = 0.0f;

        for (DailyLog log : weeklyLogs) {
            weeksWorkouts = weeksWorkouts + log.getWorkoutsCompleted();
            weeksWaterIntake = weeksWaterIntake + log.getWaterDrank();
            weeksSleep = weeksSleep + log.getSleepHours();
        }

        waterAverage = weeksWaterIntake / weeklyLogs.size();
        sleepAverage = weeksSleep / weeklyLogs.size();

        TextView workoutsTextView = findViewById(R.id.textViewWorkoutProgressionWeekly);
        ProgressBar waterProgressionBar = findViewById(R.id.progressBarWaterIntakeWeekly);
        ProgressBar sleepProgressionBar = findViewById(R.id.progressBarSleepWeekly);
        TextView dailyWaterAverage = findViewById(R.id.textViewDailyWaterAverage);
        TextView dailySleepAverage = findViewById(R.id.textViewDailySleepAverage);

        if (currentGoal != null) {
            workoutsTextView.setText("Your weekly workout goal is: " + currentGoal.getWorkoutGoal() +
                    " and you have completed " + weeksWorkouts + " workouts!");
            waterProgressionBar.setProgress((int) ((weeksWaterIntake / (currentGoal.getWaterGoal()*7) * 100)));
            sleepProgressionBar.setProgress((int) ((weeksSleep / (currentGoal.getSleepGoal()*7) * 100)));
            dailyWaterAverage.setText(String.format("%.2f liters", waterAverage));
            dailySleepAverage.setText(String.format("%.2f hours", sleepAverage));
        }

    }

    private void showDailyProgress() {
        DailyLog todaysLog = DailyLogManager.getInstance().getTodaysLog();

        if(todaysLog != null) {
            Goal goal = GoalManager.getInstance().getGoal();

            int waterProgress = (int) ((todaysLog.getWaterDrank() / goal.getWaterGoal()) * 100);
            int sleepProgress = (int) ((todaysLog.getSleepHours() / goal.getSleepGoal()) * 100);

            dailyWaterProgressBar.setProgress(waterProgress);
            dailySleepProgressBar.setProgress(sleepProgress);

            dailyWorkoutTextView.setText("Today " + todaysLog.getWorkoutsCompleted() + " workouts completed!");
        }else {
            dailyWorkoutTextView.setText("No data for today.");
        }
    }

    //https://developer.android.com/guide/navigation/navigation-custom-back
    //Reference
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("waterAverage", waterAverage);
        intent.putExtra("sleepAverage", sleepAverage);
        intent.putExtra("weeksWorkouts", weeksWorkouts);


        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}