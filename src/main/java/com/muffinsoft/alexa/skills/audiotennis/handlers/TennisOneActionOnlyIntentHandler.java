package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisOneActionOnlyIntentHandler extends TennisGameIntentHandler {

    public TennisOneActionOnlyIntentHandler(IntentFactory intentFabric) {
        super(intentFabric);
    }

    @Override
    protected String getIntentName() {
        return "OneActionOnlyIntent";
    }
}
