package com.muffinsoft.alexa.skills.audiotennis.test.models;

import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ActivityProgressTest {

    @Test
    void testCreateDefault() {
        ActivityProgress defaultActivity = ActivityProgress.createDefault();
        Assertions.assertNotNull(defaultActivity.getCurrentActivity());
    }
}
