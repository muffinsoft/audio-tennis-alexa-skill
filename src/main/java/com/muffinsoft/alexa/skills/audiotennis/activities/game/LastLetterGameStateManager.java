package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;

public class LastLetterGameStateManager extends CompetitionGameStateManager {

    public LastLetterGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
        this.activityProgress.setCurrentActivity(ActivityType.LAST_LETTER);
    }

    @Override
    protected boolean isSuccessAnswer() {

        String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
        char lastLetter = previousWord.charAt(previousWord.length() - 1);
        return checkIfSuccess(lastLetter);
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char lastLetter = getActionUserReply().charAt(getActionUserReply().length() - 1);
        return getNextWrongWordForActivityFromLetter(lastLetter);
    }

    @Override
    protected String getAlreadyUsedWordByActivityRules() {
        char lastLetter = getActionUserReply().charAt(getActionUserReply().length() - 1);
        return getAlreadyUserWord(lastLetter, this.activityProgress.getUsedWords());
    }

    @Override
    protected Character getCharWithMistakeForEnemy() {
        return getActionUserReply().charAt(getActionUserReply().length() - 1);
    }

    @Override
    protected String getNextRightWordForActivity() {
        char lastLetter = getActionUserReply().charAt(getActionUserReply().length() - 1);
        return getNextRightWordForActivity(lastLetter);
    }
}
