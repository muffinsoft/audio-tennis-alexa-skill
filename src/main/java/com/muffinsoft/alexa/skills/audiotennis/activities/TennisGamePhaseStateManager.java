package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.Map;

public class TennisGamePhaseStateManager extends TennisBaseGameStateManager {

    public TennisGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer);
    }

    @Override
    protected boolean isSuccessAnswer() {
        return false;
    }

    @Override
    protected boolean isEndWinActivityState() {
        // if
        return false;
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {
        return null;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {
        return null;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {
        return null;
    }
}
