package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

abstract class CompetitionGameStateManager extends TennisGamePhaseStateManager {

    protected char characterWithMistake;

    CompetitionGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateSuccessAnswerCounter();
        this.activityProgress.iterateEnemyAnswerCounter();

        this.activityProgress.addUsedWord(getUserReply());

        String nextWord;
        String nextRightWord = getNextRightWordForActivity();

        if (nextRightWord.isEmpty()) {
            String alreadyUserWord = getAlreadyUsedWordByActivityRules();
            nextWord = appendNextRepeatedWord(builder, alreadyUserWord);
        }
        else if (this.activityProgress.getEnemyAnswerCounter() != 0 && this.activityProgress.getEnemyAnswerCounter() % this.activityProgress.getComplexity() == 0) {
            boolean isAlreadyUsedMistake = ThreadLocalRandom.current().nextBoolean();
            String alreadyUserWord = getAlreadyUsedWordByActivityRules();
            if (isAlreadyUsedMistake && alreadyUserWord != null) {
                nextWord = appendNextRepeatedWord(builder, alreadyUserWord);
            }
            else {
                nextWord = appendNextWrongWord(builder);
            }
        }
        else {

            nextWord = nextRightWord;

            BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentReactionAfterXWordsPhrase();

            if (!randomOpponentAfterWordPhrase.isEmpty()) {
                String newContent = replaceWordPlaceholders(randomOpponentAfterWordPhrase.getContent(), nextWord, null, null);

                BasePhraseContainer phraseContainer = new BasePhraseContainer(newContent, randomOpponentAfterWordPhrase.getRole());

                builder.addResponse(getDialogTranslator().translate(phraseContainer));
            }
            else {
                builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));
            }
            this.activityProgress.addUsedWord(nextWord);
        }

        BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentAfterWordPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase));

        this.activityProgress.setPreviousWord(nextWord);

        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        BasePhraseContainer playerLosePhrase;
        this.activityProgress.iterateEnemyAnswerCounter();

        if (isWordAlreadyUser()) {
            playerLosePhrase = phrasesForActivity.getRandomPlayerLoseRepeatWordPhrase();
        }
        else {
            playerLosePhrase = phrasesForActivity.getRandomPlayerLoseWrongWordPhrase();
        }

        String newContent = replaceWordPlaceholders(playerLosePhrase.getContent(), getUserReply(), characterWithMistake, null);
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, playerLosePhrase.getRole());

        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));

        iterateEnemyScoreCounter(builder);

        this.activityProgress.addUsedWord(getUserReply());

        String nextWord = getNextRightWordForActivity();
        this.activityProgress.addUsedWord(nextWord);
        this.activityProgress.setPreviousWord(nextWord);

        builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));

        return builder.withSlotName(actionSlotName);
    }

    protected abstract String getNextRightWordForActivity();

    protected abstract String getNextWrongWordForActivity();

    protected abstract String getAlreadyUsedWordByActivityRules();

    protected abstract Character getCharWithMistakeForEnemy();

    private String appendNextWrongWord(DialogItem.Builder builder) {

        String nextWord = getNextWrongWordForActivity();
        builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));

        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomEnemyLoseWrongWordPhrase();

        String newContent = replaceWordPlaceholders(phraseContainer.getContent(), nextWord, getCharWithMistakeForEnemy(), null);
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, phraseContainer.getRole());

        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));

        iteratePlayerScoreCounter(builder);

        return nextWord;
    }

    private String appendNextRepeatedWord(DialogItem.Builder builder, String alreadyUserWord) {

        builder.addResponse(getDialogTranslator().translate(alreadyUserWord, enemyRole));

        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomEnemyLoseRepeatWordPhrase();

        String newContent = replaceWordPlaceholders(phraseContainer.getContent(), alreadyUserWord, null, null);
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, phraseContainer.getRole());

        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));

        iteratePlayerScoreCounter(builder);

        return alreadyUserWord;
    }
}
