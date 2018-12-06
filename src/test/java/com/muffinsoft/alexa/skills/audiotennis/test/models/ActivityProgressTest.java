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

    @Test
    void testComplexityFor0GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(0);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(4, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor1GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(1);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(4, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor2GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(2);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(4, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor3GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(3);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(6, defaultActivity.getComplexity());
    }
    @Test
    void testComplexityFor4GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(4);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(6, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor5GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(5);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(8, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor6GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(6);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(8, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor7GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(7);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(10, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor8GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(8);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(10, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor9GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(9);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(12, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor10GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(10);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(12, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor11GameCounter() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(11);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(14, defaultActivity.getComplexity());
    }
}
