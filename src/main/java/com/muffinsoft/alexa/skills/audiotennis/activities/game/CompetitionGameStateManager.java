package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityUnlokingStatus;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

abstract class CompetitionGameStateManager extends TennisGamePhaseStateManager {

    char characterWithMistake;

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
                builder.addResponse(getDialogTranslator().translate(replaceWordPlaceholders(randomOpponentAfterWordPhrase, nextWord, null, null)));
            }
            else {
                builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));
            }
            this.activityProgress.addUsedWord(nextWord);
        }

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

        builder.addResponse(getDialogTranslator().translate(replaceWordPlaceholders(playerLosePhrase, getUserReply(), characterWithMistake, null)));

        iterateEnemyScoreCounter(builder);

        String nextWord = getNextRightWordForActivity();
        this.activityProgress.setPreviousWord(nextWord);
        this.activityProgress.addUsedWord(nextWord);
        this.activityProgress.addUsedWord(getUserReply());

        ActivityUnlokingStatus unlockingStatus = getUnlockingStatus();
        logger.debug("Current Status: " + unlockingStatus);
        switch (unlockingStatus) {
            case UNLOCKED:
                handleEnterNewActivity(builder);
                break;
            case CONTINUE:
                handlerContinueRePrompt(builder);
                break;
            case PROCEED:
                appendNextRoundPhrase(builder);
                builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));
                break;
        }

        return builder.withSlotName(actionSlotName);
    }

    protected abstract String getNextRightWordForActivity();

    protected abstract String getNextWrongWordForActivity();

    protected abstract String getAlreadyUsedWordByActivityRules();

    protected abstract Character getCharWithMistakeForEnemy();

    protected abstract Character getNextReplyCharacter(String word);

    private String appendNextWrongWord(DialogItem.Builder builder) {

        String nextWord = getNextWrongWordForActivity();
        builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));

        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomEnemyLoseWrongWordPhrase();

        builder.addResponse(getDialogTranslator().translate(replaceWordPlaceholders(phraseContainer, nextWord, getCharWithMistakeForEnemy(), null)));

        iteratePlayerScoreCounter(builder);

        String newWord = handleUnlockingStatusAfterEnemyMistake(builder);

        return newWord != null ? newWord : nextWord;
    }

    private String appendNextRepeatedWord(DialogItem.Builder builder, String alreadyUserWord) {

        builder.addResponse(getDialogTranslator().translate(alreadyUserWord, enemyRole));

        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomEnemyLoseRepeatWordPhrase();

        builder.addResponse(getDialogTranslator().translate(replaceWordPlaceholders(phraseContainer, alreadyUserWord, null, null)));

        iteratePlayerScoreCounter(builder);

        String newWord = handleUnlockingStatusAfterEnemyMistake(builder);

        return newWord != null ? newWord : alreadyUserWord;
    }

    private String handleUnlockingStatusAfterEnemyMistake(DialogItem.Builder builder) {
        ActivityUnlokingStatus unlockingStatus = getUnlockingStatus();
        logger.debug("Current status: " + unlockingStatus);
        switch (unlockingStatus) {
            case UNLOCKED:
                handleEnterNewActivity(builder);
                return null;
            case CONTINUE:
                handlerContinueRePrompt(builder);
                return null;
            case PROCEED:
                appendNextRoundPhrase(builder);
                return appendHintAfterEnemyMistake(builder);
        }
        return null;
    }

    private String appendHintAfterEnemyMistake(DialogItem.Builder builder) {
        BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomOpponentFirstPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));

        String newWord = generateRandomWord();

        builder.addResponse(getDialogTranslator().translate(newWord, enemyRole));

        return newWord;
    }
}
