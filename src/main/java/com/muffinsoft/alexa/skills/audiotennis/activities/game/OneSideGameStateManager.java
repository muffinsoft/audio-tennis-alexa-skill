package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
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

        this.activityProgress.iterateSuccessCounter();

        if (this.activityProgress.getSuccessCounter() >= settingsForActivity.getScoresToWinRoundValue()) {
            iteratePlayerScoreCounter(builder);
        }

        List<String> words = new ArrayList<>();
        List<String> reactions = new ArrayList<>();

        for (int i = 0; i < this.activityProgress.getComplexity(); i++) {
            WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);
            words.add(nextWord.getWord());
            reactions.add(nextWord.getUserReaction());
        }

        builder.addResponse(getDialogTranslator().translate(String.join(" ", words)));

        this.activityProgress.setRequiredUserReaction(String.join(" ", reactions));

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
