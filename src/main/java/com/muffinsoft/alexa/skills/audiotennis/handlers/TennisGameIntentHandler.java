package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.handlers.GameIntentHandler;

import java.util.Map;
import java.util.Objects;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;

public abstract class TennisGameIntentHandler extends GameIntentHandler {

    protected TennisGameIntentHandler(IntentFactory intentFactory) {
        super(intentFactory);
    }

    @Override
    protected void handleInputAttributes(Map<String, Slot> slots, AttributesManager attributesManager) {

    }

    @Override
    protected IntentType getIntentFromRequest(AttributesManager attributesManager) {
        String stringifyIntent = String.valueOf(attributesManager.getSessionAttributes().get(INTENT));
        if (stringifyIntent == null || Objects.equals(stringifyIntent, "null")) {
            logger.debug("Invoked action intent handler with the default Intent Type");
            return IntentType.GAME;
        }
        else {
            logger.debug("Invoked action intent handler with the Intent Type: " + stringifyIntent);
            return IntentType.valueOf(stringifyIntent);
        }
    }
}
