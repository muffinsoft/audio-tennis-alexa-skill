package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseGameStateManager;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.Map;

public class TennisBaseGameStateManager extends BaseGameStateManager {

    private StateType stateType = StateType.ACTIVITY_INTRO;

    public TennisBaseGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager);
    }

    @Override
    protected void populateActivityVariables() {
        super.populateActivityVariables();
    }

    @Override
    protected void updateSessionAttributes() {
        super.updateSessionAttributes();
    }

    @Override
    protected void updatePersistentAttributes() {
        super.updatePersistentAttributes();
    }

    @Override
    protected DialogItem.Builder populateResponse(DialogItem.Builder builder) {

        builder = handleStateAction(stateType, builder);

        // here I will modify response

        return builder;
    }

    @Override
    protected DialogItem.Builder handleGamePhaseState(DialogItem.Builder builder) {
        return super.handleGamePhaseState(builder);
    }

    @Override
    protected DialogItem.Builder handleWinState(DialogItem.Builder builder) {
        return super.handleWinState(builder);
    }

    @Override
    protected DialogItem.Builder handleLoseState(DialogItem.Builder builder) {
        return super.handleLoseState(builder);
    }

    @Override
    protected DialogItem.Builder handleReadyToPlayState(DialogItem.Builder builder) {
        return super.handleReadyToPlayState(builder);
    }

    @Override
    protected DialogItem.Builder handleDemoState(DialogItem.Builder builder) {
        return super.handleDemoState(builder);
    }

    @Override
    protected DialogItem.Builder handleActivityIntroState(DialogItem.Builder builder) {
        return super.handleActivityIntroState(builder);
    }
}
