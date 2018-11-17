package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.handlers.HelpIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.activities.HelpStateManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

public class TennisHelpIntentHandler extends HelpIntentHandler {

    private final ConfigContainer configurationContainer;

    public TennisHelpIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.configurationContainer = configurationContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput handlerInput) {
        return new HelpStateManager(getSlotsFromInput(handlerInput), handlerInput.getAttributesManager(), configurationContainer);
    }
}