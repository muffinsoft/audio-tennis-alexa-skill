package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.sdk.util.SlotComputer;
import com.muffinsoft.alexa.skills.audiotennis.components.ObjectConvert;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;

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
        switchToActivity(slots, sessionAttributes);
    }

    private void switchToActivity(Map<String, Slot> slots, Map<String, Object> sessionAttributes) {
        Map<SlotName, List<String>> slotsBySlotName = SlotComputer.compute(slots);
        if (slotsBySlotName.containsKey(SlotName.MISSION)) {
            ActivityType activityType = ActivityType.valueOf(slotsBySlotName.get(SlotName.MISSION).get(0));
            ActivityProgress activityProgress = null;
            if (sessionAttributes.containsKey(SessionConstants.ACTIVITY_PROGRESS)) {
                LinkedHashMap rawActivityProgress = (LinkedHashMap) sessionAttributes.get(ACTIVITY_PROGRESS);
                if (rawActivityProgress != null) {
                    activityProgress = new ObjectMapper().convertValue(rawActivityProgress, ActivityProgress.class);
                    activityProgress.setCurrentActivity(activityType);
                    activityProgress.addUnlockedActivity(activityType);
                    activityProgress.reset();
                }
            }

            if (activityProgress == null) {
                activityProgress = new ActivityProgress(activityType, true);
            }

            sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
        }
    }

    @Override
    protected IntentType getIntentFromRequest(AttributesManager attributesManager) {
        return IntentType.GAME;
    }
}
