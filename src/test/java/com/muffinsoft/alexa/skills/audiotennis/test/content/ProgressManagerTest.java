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

        HashSet<String> unlockedActivities = new HashSet<>();
        unlockedActivities.add(ActivityType.LAST_LETTER.name());
        unlockedActivities.add(ActivityType.BAM_WHAM.name());

        ActivityType nextActivity = progressManager.getNextActivity(ActivityType.LAST_LETTER, unlockedActivities);

        Assertions.assertEquals(nextActivity, ActivityType.ALPHABET_RACE);
    }
}
