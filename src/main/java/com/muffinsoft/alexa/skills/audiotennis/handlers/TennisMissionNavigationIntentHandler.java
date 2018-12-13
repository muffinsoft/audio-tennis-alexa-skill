package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisMissionNavigationIntentHandler extends TennisGameIntentHandler {

    public TennisMissionNavigationIntentHandler(IntentFactory intentFabric) {
        super(intentFabric);
    }

    @Override
    protected String getIntentName() {
        return "MissionNavigationIntent";
    }
}
