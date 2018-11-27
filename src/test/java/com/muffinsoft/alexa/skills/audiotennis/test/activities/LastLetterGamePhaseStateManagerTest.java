package com.muffinsoft.alexa.skills.audiotennis.test.activities;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.LastLetterGamePhaseStateManager;
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

public class LastLetterGamePhaseStateManagerTest extends BaseStateManagerTest {

    @Test
    void testActivePhaseRightAnswer() {

        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        WordContainer randomWordForActivity = activityManager.getRandomWordForActivity(ActivityType.LAST_LETTER);

        String firstWord = randomWordForActivity.getWord();

        ActivityProgress activityProgress = new ActivityProgress();
        activityProgress.setPreviousWord(firstWord);

        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForActivityFromLetter(ActivityType.LAST_LETTER, firstWord.charAt(firstWord.length() - 1));
        String secondWord = randomWordForActivityFromLetter.getWord();
        Map<String, Slot> slots = createSlotsForValue(secondWord);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.GAME_PHASE_1);

        LastLetterGamePhaseStateManager stateManager = new LastLetterGamePhaseStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        Assertions.assertNotNull(sessionAttributes);
    }
}
