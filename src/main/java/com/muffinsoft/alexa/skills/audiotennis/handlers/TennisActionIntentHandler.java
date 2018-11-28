package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.handlers.GameIntentHandler;

import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;

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
        String stringifyIntent = String.valueOf(attributesManager.getSessionAttributes().get(INTENT));
        if (stringifyIntent == null) {
            logger.debug("Was evoked action intent handler with default Intent Type");
            return IntentType.GAME;
        }
        else {
            logger.debug("Was evoked action intent handler with Intent Type: " + stringifyIntent);
            return IntentType.valueOf(stringifyIntent);
        }
    }
}
