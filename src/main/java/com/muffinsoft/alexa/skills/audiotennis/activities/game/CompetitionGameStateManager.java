package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityUnlokingStatus;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

abstract class CompetitionGameStateManager extends TennisGamePhaseStateManager {

    private char characterWithMistake;

    CompetitionGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
    }

    protected void appendDynamicEntities(DialogItem.Builder builder) {

    }

    boolean checkIfSuccess(char letter) {
        this.characterWithMistake = letter;

        String[] possibleWordsInReply = getUserReply().toLowerCase().split(" ");

        for (String word : possibleWordsInReply) {

            char firstLetter = word.charAt(0);

            if (!Objects.equals(letter, firstLetter)) {
                continue;
            }

            if (!isWordAlreadyUsed()) {
                return true;
            }
        }
        return false;
    }

    String getAlreadyUserWord(char nextLetter, Set<String> usedWords) {
        List<String> usedWordsOnLetter = new ArrayList<>();
        for (String word : usedWords) {
            if (word.startsWith(String.valueOf(nextLetter))) {
                usedWordsOnLetter.add(word);
            }
        }
        if (usedWordsOnLetter.isEmpty()) {
            return null;
        }
        return usedWordsOnLetter.get(ThreadLocalRandom.current().nextInt(usedWordsOnLetter.size()));
    }

    String getNextWrongWordForActivityFromLetter(char letter) {
        char wrongLetter = activityManager.getRandomLetterExcept(letter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.addUsedWord(word);
        return word;
    }

    String getNextRightWordForActivity(char letter) {
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(letter, this.activityProgress.getUsedWords());
        if (randomWordForActivityFromLetter.isEmpty()) {
            return null;
        }
        return randomWordForActivityFromLetter.getWord();
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
        else if (this.activityProgress.getEnemyAnswerCounter() >= this.activityProgress.getComplexity()) {
            this.activityProgress.setEnemyAnswerCounter(0);
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

        return builder;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        BasePhraseContainer playerLosePhrase;
        this.activityProgress.iterateEnemyAnswerCounter();

        if (isWordAlreadyUsed()) {
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
                BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomOpponentFirstPhrase();
                builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));
                builder.addResponse(getDialogTranslator().translate(nextWord, enemyRole));
                break;
        }

        return builder;
    }

    protected abstract String getNextRightWordForActivity();

    protected abstract String getNextWrongWordForActivity();

    protected abstract String getAlreadyUsedWordByActivityRules();

    protected abstract Character getCharWithMistakeForEnemy();

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
                return appendHintAfterMistake(builder);
        }
        return null;
    }

    private String appendHintAfterMistake(DialogItem.Builder builder) {
        BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomOpponentFirstPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));

        String newWord = generateRandomWord();

        builder.addResponse(getDialogTranslator().translate(newWord, enemyRole));

        return newWord;
    }
}
