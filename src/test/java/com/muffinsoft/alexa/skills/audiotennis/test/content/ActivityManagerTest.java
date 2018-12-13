package com.muffinsoft.alexa.skills.audiotennis.test.content;

import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ActivityManagerTest {

    @Test
    void testIsWordKnown() {
        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        boolean isKnown = activityManager.isKnownWord(Collections.singletonList("expert"));
        Assertions.assertTrue(isKnown);
    }

    @Test
    void testIsWordUnKnown() {
        ActivityManager activityManager = IoC.provideSettingsDependencyContainer().getActivityManager();
        boolean isKnown = activityManager.isKnownWord(Collections.singletonList("asdasd"));
        Assertions.assertFalse(isKnown);
    }
}
