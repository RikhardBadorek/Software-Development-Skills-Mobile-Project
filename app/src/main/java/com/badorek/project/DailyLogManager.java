package com.badorek.project;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyLogManager {

    private ArrayList<DailyLog> dailyLogs = null;
    private static DailyLogManager dailyLogManagerInstance = null;

    private DailyLogManager() {
        dailyLogs = new ArrayList<>();
    }

    public static DailyLogManager getInstance() {
        if (dailyLogManagerInstance == null) {
            dailyLogManagerInstance = new DailyLogManager();
        }
        return dailyLogManagerInstance;
    }

    public boolean logExists(String date) {
        for (DailyLog dailyLog : dailyLogs) {
            if (dailyLog.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public void addLog(DailyLog log) {
        for (int i = 0; i < dailyLogs.size(); i++) {
            if (dailyLogs.get(i).getDate().equals(log.getDate())) {
                dailyLogs.set(i, log);
                return;
            }
        }
        dailyLogs.add(log);
    }

    public void saveLogs(Context context) {
        ObjectOutputStream logWriter = null;
        try {
            logWriter = new ObjectOutputStream(context.openFileOutput("daily_logs.data", Context.MODE_PRIVATE));
            logWriter.writeObject(dailyLogs);
            logWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadLogs(Context context) {
        ObjectInputStream logReader = null;
        try {
            logReader = new ObjectInputStream(context.openFileInput("daily_logs.data"));
            dailyLogs = (ArrayList<DailyLog>) logReader.readObject();
            logReader.close();
        } catch (IOException | ClassNotFoundException e) {
            dailyLogs = new ArrayList<>();
        }
    }

    public DailyLog getTodaysLog() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayDate = String.format("%04d-%02d-%02d", year, month, day);

        for (DailyLog log : dailyLogs) {
            if (log.getDate().equals(todayDate)) {
                return log;
            }
        }

        return null;
    }

    public DailyLog getLogByDate(String date) {
        for (DailyLog log : dailyLogs) {
            if (log.getDate().equals(date)) {
                return log;
            }
        }
        return null;
    }
}
