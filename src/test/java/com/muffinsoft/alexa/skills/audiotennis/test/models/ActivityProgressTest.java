package com.muffinsoft.alexa.skills.audiotennis.test.models;

import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivitySettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ActivityProgressTest {

    @Test
    void testCreateDefault() {
        ActivityProgress defaultActivity = ActivityProgress.createDefault();
        Assertions.assertNotNull(defaultActivity.getCurrentActivity());
    }

    @Test
    void testIsTimeToLevelUpForFirstIntersection() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(3);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        boolean timeToLevelUp = defaultActivity.isTimeToLevelUp(settings);
        Assertions.assertTrue(timeToLevelUp);
    }

    @Test
    void testIsTimeToLevelUpForWrongCase() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(4);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        boolean timeToLevelUp = defaultActivity.isTimeToLevelUp(settings);
        Assertions.assertFalse(timeToLevelUp);
    }

    @Test
    void testIsTimeToLevelUpForTooLowValue() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(1);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        boolean timeToLevelUp = defaultActivity.isTimeToLevelUp(settings);
        Assertions.assertFalse(timeToLevelUp);
    }
}
