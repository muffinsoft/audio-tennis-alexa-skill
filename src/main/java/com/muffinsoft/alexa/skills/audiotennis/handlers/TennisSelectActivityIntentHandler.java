package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisSelectActivityIntentHandler extends TennisGameIntentHandler {

    public TennisSelectActivityIntentHandler(IntentFactory intentFactory) {
        super(intentFactory);
    }

    @Override
    protected String getIntentName() {
        return "SelectActivityIntent";
    }
}
