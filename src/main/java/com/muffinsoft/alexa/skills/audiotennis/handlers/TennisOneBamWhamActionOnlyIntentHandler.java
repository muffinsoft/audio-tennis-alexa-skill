package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisOneBamWhamActionOnlyIntentHandler extends TennisGameIntentHandler {

    public TennisOneBamWhamActionOnlyIntentHandler(IntentFactory intentFabric) {
        super(intentFabric);
    }

    @Override
    protected String getIntentName() {
        return "OneBamWhamActionOnlyIntent";
    }
}
