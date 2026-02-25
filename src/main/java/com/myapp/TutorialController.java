package com.myapp;

public class TutorialController {

    private static TutorialController instance;

    private boolean active = false;
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
    }

    public boolean isActive() {
        return active;
    }

    public int getStep() {
        return step;
    }

    public void nextStep() {
        step++;
    }
}
