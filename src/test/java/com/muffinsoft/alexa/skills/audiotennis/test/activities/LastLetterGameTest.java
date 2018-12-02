package com.muffinsoft.alexa.skills.audiotennis.test.activities;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.LastLetterGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;

class LastLetterGameTest extends BaseTest {

    @Test
    void testActivePhaseRightAnswer() {

        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        WordContainer randomWordForActivity = activityManager.getRandomWordForActivity(ActivityType.LAST_LETTER);

        String firstWord = randomWordForActivity.getWord();

        ActivityProgress activityProgress = new ActivityProgress(ActivityType.LAST_LETTER);
        activityProgress.setPreviousWord(firstWord);

        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(firstWord.charAt(firstWord.length() - 1), Collections.emptySet());
        String secondWord = randomWordForActivityFromLetter.getWord();
        Map<String, Slot> slots = createSlotsForValue(secondWord);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.GAME_PHASE_1);

        LastLetterGameStateManager stateManager = new LastLetterGameStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        ActivityProgress resultActivityProgress = (ActivityProgress) sessionAttributes.get(ACTIVITY_PROGRESS);
        Assertions.assertEquals(resultActivityProgress.getSuccessCounter(), 1);
        Assertions.assertEquals(resultActivityProgress.getUsedWords().size(), 1);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }

    @Test
    void testActivePhaseWrongAnswer() {

        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        WordContainer randomWordForActivity = activityManager.getRandomWordForActivity(ActivityType.LAST_LETTER);

        String firstWord = randomWordForActivity.getWord();

        ActivityProgress activityProgress = new ActivityProgress(ActivityType.LAST_LETTER);
        activityProgress.setPreviousWord(firstWord);

        WordContainer randomWordForActivityFromWrongLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(firstWord.charAt(0), Collections.emptySet());
        String secondWord = randomWordForActivityFromWrongLetter.getWord();
        Map<String, Slot> slots = createSlotsForValue(secondWord);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.GAME_PHASE_1);

        LastLetterGameStateManager stateManager = new LastLetterGameStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        ActivityProgress resultActivityProgress = (ActivityProgress) sessionAttributes.get(ACTIVITY_PROGRESS);
        Assertions.assertEquals(resultActivityProgress.getEnemyPointCounter(), 1);
        Assertions.assertEquals(resultActivityProgress.getUsedWords().size(), 1);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }

    @Test
    void testActivePhaseEnemyWrongAnswer() {

        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        WordContainer randomWordForActivity = activityManager.getRandomWordForActivity(ActivityType.LAST_LETTER);

        String firstWord = randomWordForActivity.getWord();

        ActivityProgress activityProgress = new ActivityProgress(ActivityType.LAST_LETTER);
        activityProgress.setPreviousWord(firstWord);
        activityProgress.setEnemyAnswerCounter(3);

        WordContainer randomWordForActivityFromWrongLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(firstWord.charAt(firstWord.length() - 1), Collections.emptySet());
        String secondWord = randomWordForActivityFromWrongLetter.getWord();
        Map<String, Slot> slots = createSlotsForValue(secondWord);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACTIVITY_PROGRESS, toMap(activityProgress));
        attributes.put(STATE_TYPE, StateType.GAME_PHASE_1);

        LastLetterGameStateManager stateManager = new LastLetterGameStateManager(slots, createAttributesManager(slots, attributes), IoC.provideSettingsDependencyContainer(), IoC.providePhraseDependencyContainer());

        DialogItem dialogItem = stateManager.nextResponse();

        stateManager.updateAttributesManager();

        Map<String, Object> sessionAttributes = stateManager.getSessionAttributes();
        ActivityProgress resultActivityProgress = (ActivityProgress) sessionAttributes.get(ACTIVITY_PROGRESS);
        Assertions.assertEquals(resultActivityProgress.getPlayerPointCounter(), 1);
        Assertions.assertEquals(resultActivityProgress.getUsedWords().size(), 1);
        Assertions.assertFalse(dialogItem.getSpeech().isEmpty());
    }
}
