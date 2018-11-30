package com.muffinsoft.alexa.skills.audiotennis.test.components;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.RhymeMatchGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.components.TennisIntentFabric;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.test.activities.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;

class TennisIntentFabricTest extends BaseTest {

    @Test
    void test() {

        TennisIntentFabric intentFabric = new TennisIntentFabric(IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        Map<String, Slot> slots = createSlotsForValue("yes");

        ActivityProgress activityProgress = new ActivityProgress(ActivityType.BAM_WHAM);
        activityProgress.setPossibleActivity(ActivityType.RHYME_MATCH);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.GAME_PHASE_1);
        attributes.put(SWITCH_ACTIVITY_STEP, "yes");

        StateManager nextState = intentFabric.getNextState(IntentType.GAME, slots, createAttributesManager(slots, attributes));

        Assertions.assertEquals(nextState.getClass(), RhymeMatchGameStateManager.class);
    }
}
