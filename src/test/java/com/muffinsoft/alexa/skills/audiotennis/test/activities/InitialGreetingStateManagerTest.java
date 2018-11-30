package com.muffinsoft.alexa.skills.audiotennis.test.activities;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.activities.InitialGreetingStateManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;

public class InitialGreetingStateManagerTest extends BaseStateManagerTest {

    @Test
    void goldenPath() {

        Map<String, Slot> slots = createSlotsForValue("test");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(INTENT, IntentType.INITIAL_GREETING);
        attributes.put(USER_REPLY_BREAKPOINT, 2);

        InitialGreetingStateManager stateManager = new InitialGreetingStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        Assertions.assertEquals(sessionAttributes.get(INTENT), IntentType.GAME);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }
}
