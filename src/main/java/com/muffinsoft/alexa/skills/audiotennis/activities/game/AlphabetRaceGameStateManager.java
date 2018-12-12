package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;

public class AlphabetRaceGameStateManager extends CompetitionGameStateManager {

    public AlphabetRaceGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.ALPHABET_RACE;
    }

    @Override
    protected boolean isSuccessAnswer() {
        String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
        char firstEnemyLetter = previousWord.charAt(0);
        char nextLetter = activityManager.getNextLetter(firstEnemyLetter);
        return checkIfSuccess(nextLetter);
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char firstLetter = getUserReply().charAt(0);
        char wrongLetter = activityManager.getRandomLetterExcept(firstLetter);
        return getNextWrongWordForActivityFromLetter(wrongLetter);
    }

    @Override
    protected String getAlreadyUsedWordByActivityRules() {
        char firstLetter = getUserReply().charAt(0);
        char nextLetter = activityManager.getNextLetter(firstLetter);
        return getAlreadyUserWord(nextLetter, this.activityProgress.getUsedWords());
    }

    @Override
    protected Character getCharWithMistakeForEnemy() {
        return getUserReply().charAt(0);
    }

    @Override
    protected String getNextRightWordForActivity() {
        char firstLetter = getUserReply().charAt(0);
        char nextLetter = activityManager.getNextLetter(firstLetter);
        return getNextRightWordForActivity(nextLetter);
    }
}