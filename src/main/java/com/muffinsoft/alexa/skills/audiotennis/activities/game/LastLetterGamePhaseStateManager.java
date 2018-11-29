package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
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
    protected boolean isEndWinActivityState() {
        return this.activityProgress.getPlayerScoreCounter() == settingsForActivity.getScoresToWinRoundCounter();
    }

    @Override
    protected boolean isEndLoseActivityState() {
        return this.activityProgress.getEnemyScoreCounter() == settingsForActivity.getScoresToWinRoundCounter();
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

            return !isWordAlreadyUser();
        }
        return false;
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateSuccessCounter();

        String nextWord;
        if (this.activityProgress.getEnemyAnswerCounter() != 0 && this.activityProgress.getEnemyAnswerCounter() % this.activityProgress.getActivityEnemyMistakeIterationPointer() == 0) {
            nextWord = getNextWrongWordForActivity();
            builder.addResponse(getDialogTranslator().translate(nextWord));
            iteratePlayerScoreCounter(builder);
            builder.addResponse(getDialogTranslator().translate("Your word should starts from " + nextWord.charAt(nextWord.length() - 1)));
        }
        else {
            BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentAfterWordPhrase();
            builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase));
            nextWord = getNextRightWordForActivity();
            builder.addResponse(getDialogTranslator().translate(nextWord));
        }
        this.activityProgress.setPreviousWord(nextWord);

        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        BasePhraseContainer playerLosePhrase;

        if (isWordAlreadyUser()) {
            playerLosePhrase = phrasesForActivity.getRandomPlayerLoseRepeatWordPhrase();
        }
        else {
            playerLosePhrase = phrasesForActivity.getRandomPlayerLoseWrongWordPhrase();
        }

        builder.addResponse(getDialogTranslator().translate(playerLosePhrase));

        iterateEnemyScoreCounter(builder);

        String nextWord = getNextRightWordForActivity();
        this.activityProgress.setPreviousWord(nextWord);

        builder.addResponse(getDialogTranslator().translate(nextWord));

        return builder.withSlotName(actionSlotName);
    }

    private boolean isWordAlreadyUser() {
        return activityProgress.getUsedWords().contains(getUserReply());
    }

    private String getNextWrongWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        char wrongLetter = activityManager.getRandomLetterExcept(lastLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.iterateEnemyAnswerCounter();
        this.activityProgress.addUsedWord(word);
        return word;
    }

    private String getNextRightWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, lastLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.iterateEnemyAnswerCounter();
        this.activityProgress.addUsedWord(word);
        return word;
    }
}
