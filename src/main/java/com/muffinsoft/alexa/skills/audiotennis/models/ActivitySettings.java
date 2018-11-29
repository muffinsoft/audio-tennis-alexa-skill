package com.muffinsoft.alexa.skills.audiotennis.models;

public class ActivitySettings {

    private String name;
    private int startWrongPointPositionValue;
    private int iterateWrongPointPositionEveryLevels;
    private int addendToWrongPointPosition;
    private int scoresToWinRoundCounter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartWrongPointPositionValue() {
        return startWrongPointPositionValue;
    }

    public void setStartWrongPointPositionValue(int startWrongPointPositionValue) {
        this.startWrongPointPositionValue = startWrongPointPositionValue;
    }

    public int getIterateWrongPointPositionEveryLevels() {
        return iterateWrongPointPositionEveryLevels;
    }

    public void setIterateWrongPointPositionEveryLevels(int iterateWrongPointPositionEveryLevels) {
        this.iterateWrongPointPositionEveryLevels = iterateWrongPointPositionEveryLevels;
    }

    public int getAddendToWrongPointPosition() {
        return addendToWrongPointPosition;
    }

    public void setAddendToWrongPointPosition(int addendToWrongPointPosition) {
        this.addendToWrongPointPosition = addendToWrongPointPosition;
    }

    public int getScoresToWinRoundCounter() {
        return scoresToWinRoundCounter;
    }

    public void setScoresToWinRoundCounter(int scoresToWinRoundCounter) {
        this.scoresToWinRoundCounter = scoresToWinRoundCounter;
    }
}
