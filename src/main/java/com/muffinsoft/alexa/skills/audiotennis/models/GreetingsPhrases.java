package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GreetingsPhrases {
    private List<BasePhraseContainer> firstTimeGreeting = Collections.emptyList();
    private List<BasePhraseContainer> playerWithAwardsGreeting = Collections.emptyList();
    private List<BasePhraseContainer> playerWithoutAwardsGreeting = Collections.emptyList();
    private List<BasePhraseContainer> returnEnemyLastScore = Collections.emptyList();
    private List<BasePhraseContainer> returnPlayerLastScore = Collections.emptyList();

    public List<BasePhraseContainer> getFirstTimeGreeting() {
        return firstTimeGreeting;
    }

    public void setFirstTimeGreeting(List<BasePhraseContainer> firstTimeGreeting) {
        this.firstTimeGreeting = firstTimeGreeting;
    }

    public List<BasePhraseContainer> getPlayerWithAwardsGreeting() {
        return playerWithAwardsGreeting;
    }

    public void setPlayerWithAwardsGreeting(List<BasePhraseContainer> playerWithAwardsGreeting) {
        this.playerWithAwardsGreeting = playerWithAwardsGreeting;
    }

    public List<BasePhraseContainer> getPlayerWithoutAwardsGreeting() {
        return playerWithoutAwardsGreeting;
    }

    public void setPlayerWithoutAwardsGreeting(List<BasePhraseContainer> playerWithoutAwardsGreeting) {
        this.playerWithoutAwardsGreeting = playerWithoutAwardsGreeting;
    }

    public List<BasePhraseContainer> getReturnEnemyLastScore() {
        return returnEnemyLastScore;
    }

    public void setReturnEnemyLastScore(List<BasePhraseContainer> returnEnemyLastScore) {
        this.returnEnemyLastScore = returnEnemyLastScore;
    }

    public List<BasePhraseContainer> getReturnPlayerLastScore() {
        return returnPlayerLastScore;
    }

    public void setReturnPlayerLastScore(List<BasePhraseContainer> returnPlayerLastScore) {
        this.returnPlayerLastScore = returnPlayerLastScore;
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyLastScore() {
        if (returnEnemyLastScore.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(returnEnemyLastScore.size());
        return returnEnemyLastScore.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLastScore() {
        if (returnPlayerLastScore.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(returnPlayerLastScore.size());
        return returnPlayerLastScore.get(index);
    }

    @JsonIgnore
    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }
}
