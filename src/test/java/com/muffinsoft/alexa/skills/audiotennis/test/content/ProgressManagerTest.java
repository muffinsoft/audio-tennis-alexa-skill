package com.muffinsoft.alexa.skills.audiotennis.test.content;

import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class ProgressManagerTest {

    @Test
    void getNextActivity() {
        ProgressManager progressManager = IoC.provideProgressManager();

        HashSet<ActivityType> unlockedActivities = new HashSet<>();
        unlockedActivities.add(ActivityType.LAST_LETTER);
        unlockedActivities.add(ActivityType.BAM_WHAM);

        ActivityType nextActivity = progressManager.getNextActivity(unlockedActivities);

        Assertions.assertEquals(nextActivity, ActivityType.ALPHABET_RACE);
    }
}
