package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.handlers.GameIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.Map;

public class TennisActionIntentHandler extends GameIntentHandler {

    private final ConfigContainer configContainer;

    public TennisActionIntentHandler(ConfigContainer configContainer) {
        this.configContainer = configContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {

        AttributesManager attributesManager = input.getAttributesManager();

        Map<String, Slot> slots = getSlotsFromInput(input);

        return null;
    }
}
