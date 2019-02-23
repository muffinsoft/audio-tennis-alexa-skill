package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisSelectActivityOnlyIntentHandler extends TennisGameIntentHandler {

    public TennisSelectActivityOnlyIntentHandler(IntentFactory intentFactory) {
        super(intentFactory);
    }

    @Override
    protected String getIntentName() {
        return "SelectActivityOnlyIntent";
    }
}
