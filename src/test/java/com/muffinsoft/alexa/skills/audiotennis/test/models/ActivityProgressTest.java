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
    void testIsTimeToLevelUpForFirstIntersectionForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(3);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        boolean timeToLevelUp = defaultActivity.isTimeToLevelUp(settings);
        Assertions.assertTrue(timeToLevelUp);
    }

    @Test
    void testIsTimeToLevelUpForWrongCaseForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(4);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        boolean timeToLevelUp = defaultActivity.isTimeToLevelUp(settings);
        Assertions.assertFalse(timeToLevelUp);
    }

    @Test
    void testIsTimeToLevelUpForTooLowValueForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(1);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        boolean timeToLevelUp = defaultActivity.isTimeToLevelUp(settings);
        Assertions.assertFalse(timeToLevelUp);
    }

    @Test
    void testComplexityFor0GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(0);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(4, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor1GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(1);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(4, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor2GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(2);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(4, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor3GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(3);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(6, defaultActivity.getComplexity());
    }
    @Test
    void testComplexityFor4GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(4);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(6, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor5GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(5);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(8, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor6GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(6);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(8, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor7GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(7);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(10, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor8GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(8);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(10, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor9GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(9);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(12, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor10GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(10);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(12, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor11GameCounterForCompetition() {

        ActivityType type = ActivityType.LAST_LETTER;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(11);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(14, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor0GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(0);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(1, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor1GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(1);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(1, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor2GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(2);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(1, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor3GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(3);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(2, defaultActivity.getComplexity());
    }
    @Test
    void testComplexityFor4GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(4);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(2, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor5GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(5);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor6GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(6);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor7GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(7);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor8GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(8);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor9GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(9);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor10GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(10);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }

    @Test
    void testComplexityFor11GameCounterForOneSide() {

        ActivityType type = ActivityType.BAM_WHAM;

        ActivityProgress defaultActivity = new ActivityProgress(type);
        defaultActivity.setPlayerGameCounter(11);

        ActivitySettings settings = IoC.provideSettingsDependencyContainer().getActivityManager().getSettingsForActivity(type);

        defaultActivity.updateWithDifficultSettings(settings);
        Assertions.assertEquals(3, defaultActivity.getComplexity());
    }
}
