package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.ArrayList;
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
            WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);
            if (nextWord.isEmpty()) {
                return handleMistakeAnswer(builder);
            }
            words.add(nextWord.getWord());
            reactions.add(nextWord.getUserReaction());
        }

        this.activityProgress.iterateSuccessAnswerCounter();

        if (this.activityProgress.getSuccessCounter() >= settingsForActivity.getScoresToWinRoundValue()) {
            iteratePlayerScoreCounter(builder);
        }

        BasePhraseContainer randomOpponentAfterXWordPhrase = phrasesForActivity.getRandomOpponentReactionAfterXWordsPhrase();

        String nextWord = String.join(" ", words);

        if (randomOpponentAfterXWordPhrase.isEmpty()) {
            builder.addResponse(getDialogTranslator().translate(nextWord));
        }
        else {
            String newContent = replaceWordPlaceholders(randomOpponentAfterXWordPhrase.getContent(), nextWord, null, null);
            randomOpponentAfterXWordPhrase.setContent(newContent);
            builder.addResponse(getDialogTranslator().translate(randomOpponentAfterXWordPhrase));
        }

        this.activityProgress.setRequiredUserReaction(String.join(" ", reactions));

        BasePhraseContainer randomOpponentAfterWordPhrase = phrasesForActivity.getRandomOpponentAfterWordPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentAfterWordPhrase));

        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateMistakeCount();

        if (this.activityProgress.getMistakeCount() >= settingsForActivity.getAvailableLives()) {
            iterateEnemyScoreCounter(builder);
        }

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);

        builder.addResponse(getDialogTranslator().translate(nextWord.getWord()));

        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());

        return builder.withSlotName(actionSlotName);
    }
}
