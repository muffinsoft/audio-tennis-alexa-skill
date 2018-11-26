package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.handlers.GameIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;

import java.util.Map;

public class TennisActionIntentHandler extends GameIntentHandler {

    public TennisActionIntentHandler(IntentFactory intentFabric) {
        super(intentFabric);
    }

    @Override
    protected void handleAttributes(AttributesManager attributesManager) {
        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
    }

    @Override
    protected IntentType getIntentFromRequest(AttributesManager attributesManager) {

        return IntentType.valueOf(
                String.valueOf(
                        attributesManager.getSessionAttributes().getOrDefault(SessionConstants.INTENT, IntentType.GAME)));
    }
}
