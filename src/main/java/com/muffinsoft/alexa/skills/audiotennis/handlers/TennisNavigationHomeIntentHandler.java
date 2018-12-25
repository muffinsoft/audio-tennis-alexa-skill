package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.components.IntentFactory;

public class TennisNavigationHomeIntentHandler extends TennisGameIntentHandler {

    public TennisNavigationHomeIntentHandler(IntentFactory intentFactory) {
        super(intentFactory);
    }

    @Override
    protected String getIntentName() {
        return "AMAZON.NavigateHomeIntent";
    }
}
