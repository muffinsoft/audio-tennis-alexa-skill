package com.muffinsoft.alexa.skills.audiotennis.test.activities;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.RhymeMatchGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;

class RhymeMatchGameTest extends BaseTest {

    @Test
    void testActivePhaseWrongAnswer() {

        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        WordContainer wordForActivity = activityManager.getRandomWordForActivity(ActivityType.RHYME_MATCH);

        ActivityProgress activityProgress = new ActivityProgress(ActivityType.RHYME_MATCH);
        activityProgress.setPreviousWord(wordForActivity.getWord());
        activityProgress.setRequiredUserReaction(wordForActivity.getUserReaction());

        Map<String, Slot> slots = createSlotsForValue(SlotName.ACTION, "test");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.GAME_PHASE_1);

        RhymeMatchGameStateManager stateManager = new RhymeMatchGameStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        ActivityProgress resultActivityProgress = (ActivityProgress) sessionAttributes.get(ACTIVITY_PROGRESS);
        Assertions.assertEquals(resultActivityProgress.getEnemyPointCounter(), 1);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }

    @Test
    void testActivePhaseRightAnswer() {

        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        WordContainer wordForActivity = activityManager.getRandomWordForActivity(ActivityType.RHYME_MATCH);

        ActivityProgress activityProgress = new ActivityProgress(ActivityType.RHYME_MATCH);
        activityProgress.setPreviousWord(wordForActivity.getWord());
        activityProgress.setRequiredUserReaction(wordForActivity.getUserReaction());
        activityProgress.setComplexity(2);

        Map<String, Slot> slots = createSlotsForValue(SlotName.ACTION, "test");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.READY);

        RhymeMatchGameStateManager stateManager = new RhymeMatchGameStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        ActivityProgress resultActivityProgress = (ActivityProgress) sessionAttributes.get(ACTIVITY_PROGRESS);
        Assertions.assertEquals(resultActivityProgress.getEnemyPointCounter(), 1);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }
}
