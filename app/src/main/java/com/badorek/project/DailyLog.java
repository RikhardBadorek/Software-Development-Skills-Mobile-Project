package com.badorek.project;

import java.io.Serializable;

public class DailyLog implements Serializable {
    public int workoutsCompleted;
    public float waterDrank;
    public float sleepHours;
    public String date;

    public DailyLog(int workoutsCompleted, float waterDrank, float sleepHours, String date) {
        this.workoutsCompleted = workoutsCompleted;
        this.waterDrank = waterDrank;
        this.sleepHours = sleepHours;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getWorkoutsCompleted() {
        return workoutsCompleted;
    }

    public float getWaterDrank() {
        return waterDrank;
    }

    public float getSleepHours() {
        return sleepHours;
    }
}
