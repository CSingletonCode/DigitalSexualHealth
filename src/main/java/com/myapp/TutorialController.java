package com.myapp;

public class TutorialController {

    private static TutorialController instance;

    private boolean active = DatabaseManager.isAppointmentTutorialCompleted(userSession.getInstance().getUserId());
    private boolean completed = false;
    private int step = 0;

    private TutorialController() {}

    public static TutorialController getInstance() {
        if (instance == null) {
            instance = new TutorialController();
        }
        return instance;
    }

    public void start() {
        active = true;
        step = 0;
    }

    public void stop() {
        DatabaseManager.updateTutorialStatus(userSession.getInstance().getUserId());
        active = false;
        completed = true;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getStep() {
        return step;
    }

    public void nextStep() {
        step++;
    }
}
