package com.badorek.project;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GoalManager {
    private Goal goal = null;

    private static GoalManager goalManagerInstance = null;

    private GoalManager() {
    }

    public static GoalManager getInstance() {
        if (goalManagerInstance == null) {
            goalManagerInstance = new GoalManager();
        }
        return goalManagerInstance;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Goal getGoal() {
        return goal;
    }

    public void saveGoal(Context context) {
        ObjectOutputStream goalWriter = null;
        try {
            goalWriter = new ObjectOutputStream(context.openFileOutput("goal.data", Context.MODE_PRIVATE));
            goalWriter.writeObject(goal);
            goalWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGoal(Context context) {
        ObjectInputStream goalReader = null;
        try {
            goalReader = new ObjectInputStream(context.openFileInput("goal.data"));
            goal = (Goal) goalReader.readObject();
            goalReader.close();
        } catch (IOException | ClassNotFoundException e) {
            goal = null;
        }
    }

    public void deleteGoals() {
        goal = null;
    }
}
