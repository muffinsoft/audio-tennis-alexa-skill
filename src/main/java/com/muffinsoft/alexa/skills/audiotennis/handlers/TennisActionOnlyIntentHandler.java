package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisActionOnlyIntentHandler extends TennisGameIntentHandler {

    public TennisActionOnlyIntentHandler(IntentFactory intentFabric) {
        super(intentFabric);
    }

    @Override
    protected String getIntentName() {
        return "ActionOnlyIntent";
    }
}
