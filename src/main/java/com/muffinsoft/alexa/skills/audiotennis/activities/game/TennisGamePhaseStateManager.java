package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.Map;

public abstract class TennisGamePhaseStateManager extends TennisBaseGameStateManager {

    TennisGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer);
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {
        this.activityProgress.iterateSuccessCounter();
        generateNextWord();
        return null;
    }

    private void generateNextWord() {

    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {
        this.activityProgress.iterateMistakeCounter();

        if (this.activityProgress.getMistakeCounter() >= 0) {
            this.stateType = StateType.LOSE;
        }
        generateNextWord();
        return null;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {
        return null;
    }
}
