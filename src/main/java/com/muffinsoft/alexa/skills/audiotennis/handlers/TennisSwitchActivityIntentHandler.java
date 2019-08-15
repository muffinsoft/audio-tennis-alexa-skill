package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.PurchaseState;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.skills.audiotennis.components.ObjectConvert;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.*;
import static com.muffinsoft.alexa.skills.audiotennis.components.ActivityPuller.getActivityFromReply;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.PURCHASE_STATE;

public class TennisSwitchActivityIntentHandler extends TennisGameIntentHandler {

    public TennisSwitchActivityIntentHandler(IntentFactory intentFactory) {
        super(intentFactory);
    }

    @Override
    protected String getIntentName() {
        return "SwitchActivityIntent";
    }

    @Override
    protected void handleInputAttributes(Map<String, Slot> slots, AttributesManager attributesManager) {
        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();
        switchToActivity(slots, sessionAttributes, attributesManager);
    }

    private void switchToActivity(Map<String, Slot> slots, Map<String, Object> sessionAttributes, AttributesManager attributesManager) {
        ActivityType activityType = getActivityFromReply(slots);
        if (activityType != null) {
            if (activityType == ActivityType.ALPHABET_RACE || activityType == ActivityType.RHYME_MATCH) {
                boolean isPurchasable = (boolean) sessionAttributes.getOrDefault("isPurchasable", false);

                String state = String.valueOf(attributesManager.getPersistentAttributes().getOrDefault(PURCHASE_STATE, PurchaseState.NOT_ENTITLED));
                PurchaseState purchaseState = PurchaseState.valueOf(state);

                if (purchaseState != PurchaseState.ENTITLED && sessionAttributes.containsKey(activityType.name())) {
                    if (isPurchasable) {
                        sessionAttributes.put(INTENT, IntentType.UPSELL);
                    } else {
                        sessionAttributes.put(INTENT, IntentType.NEW_OR_MENU);
                    }
                    return;
                }
                else {
                    sessionAttributes.put(activityType.name(), "true");
                }
            }
            ActivityProgress activityProgress = null;
            if (sessionAttributes.containsKey(SessionConstants.ACTIVITY_PROGRESS)) {
                LinkedHashMap rawActivityProgress = (LinkedHashMap) sessionAttributes.get(ACTIVITY_PROGRESS);
                if (rawActivityProgress != null) {
                    activityProgress = new ObjectMapper().convertValue(rawActivityProgress, ActivityProgress.class);
                    activityProgress.setCurrentActivity(activityType);
                    activityProgress.addUnlockedActivity(activityType);
                    activityProgress.setTransition(true);
                }
            }

            if (activityProgress == null) {
                activityProgress = new ActivityProgress(activityType, true);
            }

            sessionAttributes.put(STATE_TYPE, StateType.ACTIVITY_INTRO);
            sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
        }
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
