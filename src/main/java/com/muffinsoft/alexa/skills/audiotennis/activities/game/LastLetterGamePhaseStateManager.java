package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Map;
import java.util.Objects;

public class LastLetterGamePhaseStateManager extends TennisGamePhaseStateManager {

    public LastLetterGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
    }

    @Override
    protected boolean isSuccessAnswer() {

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord();
            char lastLetter = previousWord.charAt(previousWord.length() - 1);

            String userReply = getUserReply();
            char firstLetter = userReply.charAt(0);

            if (!Objects.equals(lastLetter, firstLetter)) {
                return false;
            }

            return !activityProgress.getUsedWords().contains(getUserReply());
        }
        return false;
    }

    @Override
    protected boolean isEndWinActivityState() {
        return this.activityProgress.getEnemyMistakeCounter() == activityManager.getSettingsForActivity(this.currentActivityType).getMaxMistakeCounter();
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateSuccessCounter();

        String nextWord;
        if (this.activityProgress.getEnemySuccessCounter() != 0 && this.activityProgress.getEnemySuccessCounter() % this.activityProgress.getActivityEnemyMistakeIterationPointer() == 0) {
            nextWord = getNextWrongWordForActivity();
            builder.addResponse(getDialogTranslator().translate(nextWord));
        }
        else {
            BasePhraseContainer randomOpponentAfterWordPhrase = activitiesPhraseManager.getPhrasesForActivity(this.currentActivityType).getRandomOpponentAfterWordPhrase();
            builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase));
            nextWord = getNextRightWordForActivity();
            builder.addResponse(getDialogTranslator().translate(nextWord));
        }
        this.activityProgress.setPreviousWord(nextWord);

        return builder;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateMistakeCounter();

        if (this.activityProgress.getMistakeCounter() >= 0) {
            this.stateType = StateType.LOSE;
        }

        String nextWord = getNextRightWordForActivity();
        this.activityProgress.setPreviousWord(nextWord);

        builder.addResponse(getDialogTranslator().translate(nextWord));

        return builder;
    }

    private String getNextWrongWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        char wrongLetter = activityManager.getRandomLetterExcept(lastLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.iterateEnemyMistakeCounter();
        this.activityProgress.addUsedWord(word);
        return word;
    }

    private String getNextRightWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, lastLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.iterateEnemySuccessCounter();
        this.activityProgress.addUsedWord(word);
        return word;
    }
}
