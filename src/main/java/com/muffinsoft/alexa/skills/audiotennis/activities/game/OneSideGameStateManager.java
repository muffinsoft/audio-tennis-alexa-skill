package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

abstract class OneSideGameStateManager extends TennisGamePhaseStateManager {

    OneSideGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        List<String> words = new ArrayList<>();
        List<String> reactions = new ArrayList<>();

        for (int i = 0; i < this.activityProgress.getComplexity(); i++) {
            WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType, new HashSet<>(Arrays.asList(this.activityProgress.getPreviousWord().split(" "))));
            if (nextWord.isEmpty()) {
                return handleMistakeAnswer(builder);
            }
            words.add(nextWord.getWord());
            reactions.add(nextWord.getUserReaction());
        }

        this.activityProgress.iterateSuccessAnswerCounter();

        if (this.activityProgress.getSuccessCounter() >= settingsForActivity.getSuccessAnswersToGetScoreValue()) {
            iteratePlayerScoreCounter(builder);
            this.activityProgress.setSuccessCounter(0);
            this.activityProgress.setMistakeCount(0);
            appendNextRoundPhrase(builder);
        }

        switch (getUnlockingStatus()) {
            case UNLOCKED:
                handleEnterNewActivity(builder);
                break;
            case CONTINUE:
                handlerContinueRePrompt(builder);
                break;
            case PROCEED:
                appendSuccessFlow(builder, words, reactions);
                break;
        }

        return builder;
    }

    @Override
    String generateRandomWord() {

        List<String> words = new ArrayList<>();
        List<String> reactions = new ArrayList<>();

        for (int i = 0; i < this.activityProgress.getComplexity(); i++) {
            WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);
            words.add(nextWord.getWord());
            reactions.add(nextWord.getUserReaction());
        }

        String nextWord = String.join(" ", words);
        String requestedReactions = String.join(" ", reactions);

        this.activityProgress.setPreviousWord(nextWord);
        this.activityProgress.setRequiredUserReaction(requestedReactions);

        return nextWord;
    }

    private void appendSuccessFlow(DialogItem.Builder builder, List<String> words, List<String> reactions) {
        BasePhraseContainer randomOpponentAfterXWordPhrase = phrasesForActivity.getRandomOpponentReactionAfterXWordsPhrase();

        if (!randomOpponentAfterXWordPhrase.isEmpty()) {
            builder.addResponse(getDialogTranslator().translate(randomOpponentAfterXWordPhrase, false));
        }
        for (String word : words) {
            builder.addResponse(getAudioForWord(word));
        }

        this.activityProgress.setPreviousWord(String.join(" ", words));
        this.activityProgress.setRequiredUserReaction(String.join(" ", reactions));

        BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentAfterWordPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase, false));
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateMistakeCount();

        if (this.activityProgress.getMistakeCount() >= settingsForActivity.getAvailableLives()) {
            iterateEnemyScoreCounter(builder);
            appendNextRoundPhrase(builder);
        }

        switch (getUnlockingStatus()) {
            case UNLOCKED:
                handleEnterNewActivity(builder);
                break;
            case CONTINUE:
                handlerContinueRePrompt(builder);
                break;
            case PROCEED:
                handleMistakeFlow(builder);
                break;
        }

        return builder;
    }

    private void handleMistakeFlow(DialogItem.Builder builder) {

        if (this.activityProgress.getMistakeCount() >= settingsForActivity.getAvailableLives()) {
            this.activityProgress.setMistakeCount(0);
            this.activityProgress.setSuccessCounter(0);

            BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomOpponentFirstPhrase();
            builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase, false));
        }

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);

        builder.addResponse(getAudioForWord(nextWord.getWord()));

        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());
    }
}
