package com.badorek.project;

import java.io.Serializable;

public class Goal implements Serializable {
    public int workoutGoal;
    public int waterGoal;
    public int sleepGoal;

    public Goal(int workoutGoal, int waterGoal, int sleepGoal) {
        this.workoutGoal = workoutGoal;
        this.waterGoal = waterGoal;
        this.sleepGoal = sleepGoal;
    }

    public int getWorkoutGoal() {
        return workoutGoal;
    }

    public int getWaterGoal() {
        return waterGoal;
    }

    public int getSleepGoal() {
        return sleepGoal;
    }
}
