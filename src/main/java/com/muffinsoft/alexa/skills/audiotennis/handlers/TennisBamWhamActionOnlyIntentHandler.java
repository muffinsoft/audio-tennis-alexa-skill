package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisBamWhamActionOnlyIntentHandler extends TennisGameIntentHandler {

    public TennisBamWhamActionOnlyIntentHandler(IntentFactory intentFabric) {
        super(intentFabric);
    }

    @Override
    protected String getIntentName() {
        return "BamWhamActionOnlyIntent";
    }
}
