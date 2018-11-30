package com.muffinsoft.alexa.skills.audiotennis.models;

import java.util.Map;

public class ActivitySettings {

    private String name;
    private int startComplexityValue;
    private int iterateComplexityEveryScoresValue;
    private int addToComplexityValue;
    private int scoresToWinRoundValue;
    private int availableLives;
    private int successAnswersToGetScoreValue;
    private Map<String, String> wordsToReactions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartComplexityValue() {
        return startComplexityValue;
    }

    public void setStartComplexityValue(int startComplexityValue) {
        this.startComplexityValue = startComplexityValue;
    }

    public int getIterateComplexityEveryScoresValue() {
        return iterateComplexityEveryScoresValue;
    }

    public void setIterateComplexityEveryScoresValue(int iterateComplexityEveryScoresValue) {
        this.iterateComplexityEveryScoresValue = iterateComplexityEveryScoresValue;
    }

    public int getAddToComplexityValue() {
        return addToComplexityValue;
    }

    public void setAddToComplexityValue(int addToComplexityValue) {
        this.addToComplexityValue = addToComplexityValue;
    }

    public int getAvailableLives() {
        return availableLives;
    }

    public void setAvailableLives(int availableLives) {
        this.availableLives = availableLives;
    }

    public int getSuccessAnswersToGetScoreValue() {
        return successAnswersToGetScoreValue;
    }

    public void setSuccessAnswersToGetScoreValue(int successAnswersToGetScoreValue) {
        this.successAnswersToGetScoreValue = successAnswersToGetScoreValue;
    }

    public int getScoresToWinRoundValue() {
        return scoresToWinRoundValue;
    }

    public void setScoresToWinRoundValue(int scoresToWinRoundValue) {
        this.scoresToWinRoundValue = scoresToWinRoundValue;
    }

    public Map<String, String> getWordsToReactions() {
        return wordsToReactions;
    }

    public void setWordsToReactions(Map<String, String> wordsToReactions) {
        this.wordsToReactions = wordsToReactions;
    }
}
