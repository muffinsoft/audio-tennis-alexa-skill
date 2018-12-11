package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Map;
import java.util.Set;

public class LastLetterGameStateManager extends CompetitionGameStateManager {

    public LastLetterGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
    }

    @Override
    protected void appendDynamicEntities(DialogItem.Builder builder) {
        String previousWord = this.activityProgress.getPreviousWord();
        if (previousWord != null && !previousWord.isEmpty()) {
            char nextLetter = previousWord.charAt(previousWord.length() - 1);
            Set<String> words = activityManager.getAllWordsFromLetter(nextLetter);
            builder.withDynamicEntities(words);
        }
    }

    @Override
    protected boolean isSuccessAnswer() {

        String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
        char lastLetter = previousWord.charAt(previousWord.length() - 1);
        return checkIfSuccess(lastLetter);
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        return getNextWrongWordForActivityFromLetter(lastLetter);
    }

    @Override
    protected String getAlreadyUsedWordByActivityRules() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        return getAlreadyUserWord(lastLetter, this.activityProgress.getUsedWords());
    }

    @Override
    protected Character getCharWithMistakeForEnemy() {
        return getUserReply().charAt(getUserReply().length() - 1);
    }

    @Override
    protected String getNextRightWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(lastLetter, this.activityProgress.getUsedWords());
        if (randomWordForActivityFromLetter.isEmpty()) {
            return null;
        }
        return randomWordForActivityFromLetter.getWord();
    }
}
