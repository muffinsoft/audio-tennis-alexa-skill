package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;

abstract class CompetitionGameStateManager extends TennisGamePhaseStateManager {

    protected char characterWithMistake;

    CompetitionGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateSuccessAnswerCounter();

        String nextWord;
        String nextRightWord = getNextRightWordForActivity();
        if (nextRightWord.isEmpty() || this.activityProgress.getEnemyAnswerCounter() != 0 && this.activityProgress.getEnemyAnswerCounter() % this.activityProgress.getComplexity() == 0) {
            nextWord = getNextWrongWordForActivity();
            builder.addResponse(getDialogTranslator().translate(nextWord));
            iteratePlayerScoreCounter(builder);
            addNextWordAfterEnemyWrongAnswer(builder, nextWord);
        }
        else {

            BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentReactionAfterXWordsPhrase();
            builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase));

            nextWord = nextRightWord;
            this.activityProgress.addUsedWord(nextWord);
            builder.addResponse(getDialogTranslator().translate(nextWord));
        }

        BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentAfterWordPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase));

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

        String newContent = replaceWordPlaceholders(playerLosePhrase.getContent(), getUserReply(), characterWithMistake, null);
        playerLosePhrase.setContent(newContent);

        builder.addResponse(getDialogTranslator().translate(playerLosePhrase));

        iterateEnemyScoreCounter(builder);

        String nextWord = getNextRightWordForActivity();
        this.activityProgress.addUsedWord(nextWord);
        this.activityProgress.setPreviousWord(nextWord);

        builder.addResponse(getDialogTranslator().translate(nextWord));

        return builder.withSlotName(actionSlotName);
    }

    protected abstract void addNextWordAfterEnemyWrongAnswer(DialogItem.Builder builder, String nextWord);

    protected abstract String getNextRightWordForActivity();

    protected abstract String getNextWrongWordForActivity();
}
