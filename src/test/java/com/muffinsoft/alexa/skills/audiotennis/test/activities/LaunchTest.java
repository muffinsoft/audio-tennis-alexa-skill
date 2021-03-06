package com.muffinsoft.alexa.skills.audiotennis.test.activities;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.activities.LaunchStateManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;

class LaunchTest extends BaseTest {

    @Test
    void goldenPath() {

        Map<String, Slot> slots = createSlotsForValue(SlotName.ACTION, "test");

        Map<String, Object> attributes = new HashMap<>();

        LaunchStateManager stateManager = new LaunchStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        Assertions.assertEquals(sessionAttributes.get(INTENT), IntentType.INITIAL_GREETING);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }
}
