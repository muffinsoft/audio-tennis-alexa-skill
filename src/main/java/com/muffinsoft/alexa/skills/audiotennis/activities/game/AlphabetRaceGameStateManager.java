package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Map;
import java.util.Objects;

public class AlphabetRaceGameStateManager extends DictionaryGameStateManager {

    public AlphabetRaceGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.ALPHABET_RACE;
    }

    @Override
    protected void addNextWordAfterEnemyWrongAnswer(DialogItem.Builder builder, String nextWord) {
        char nextLetter = activityManager.getNextLetter(nextWord.charAt(0));
        builder.addResponse(getDialogTranslator().translate("Your word should starts from " + nextLetter));
    }

    @Override
    protected boolean isSuccessAnswer() {

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
            char firstEnemyLetter = previousWord.charAt(0);
            char nextLetter = activityManager.getNextLetter(firstEnemyLetter);

            String userReply = getUserReply().toLowerCase();
            char firstPlayerLetter = userReply.charAt(0);

            if (!Objects.equals(nextLetter, firstPlayerLetter)) {
                return false;
            }

            return !isWordAlreadyUser();
        }
        return false;
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char firstLetter = getUserReply().charAt(0);
        char wrongLetter = activityManager.getRandomLetterExcept(firstLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.iterateEnemyAnswerCounter();
        this.activityProgress.addUsedWord(word);
        return word;
    }

    @Override
    protected String getNextRightWordForActivity() {
        char firstLetter = getUserReply().charAt(0);
        char nextLetter = activityManager.getNextLetter(firstLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, nextLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.iterateEnemyAnswerCounter();
        this.activityProgress.addUsedWord(word);
        return word;
    }
}