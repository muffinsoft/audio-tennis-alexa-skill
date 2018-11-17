package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.handlers.LaunchRequestHandler;
import com.muffinsoft.alexa.skills.audiotennis.activities.LaunchStateManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

public class TennisLaunchRequestHandler extends LaunchRequestHandler {

    private final ConfigContainer configContainer;

    public TennisLaunchRequestHandler(ConfigContainer configContainer) {
        super();
        this.configContainer = configContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {
        return new LaunchStateManager(getSlotsFromInput(input), input.getAttributesManager(), configContainer);
    }
}
