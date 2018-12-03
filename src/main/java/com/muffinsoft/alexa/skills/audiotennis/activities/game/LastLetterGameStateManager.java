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

public class LastLetterGameStateManager extends CompetitionGameStateManager {

    public LastLetterGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
    }

    @Override
    protected void addNextWordAfterEnemyWrongAnswer(DialogItem.Builder builder, String nextWord) {
        builder.addResponse(getDialogTranslator().translate("Your word should starts from " + nextWord.charAt(nextWord.length() - 1)));
    }

    @Override
    protected boolean isSuccessAnswer() {

        boolean isSuccess = false;

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
            char lastLetter = previousWord.charAt(previousWord.length() - 1);

            this.characterWithMistake = lastLetter;

            String userReply = getUserReply().toLowerCase();
            char firstLetter = userReply.charAt(0);

            if (!Objects.equals(lastLetter, firstLetter)) {
                return false;
            }

            isSuccess = !isWordAlreadyUser();
        }
        return isSuccess;
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        char wrongLetter = activityManager.getRandomLetterExcept(lastLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.addUsedWord(word);
        return word;
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
