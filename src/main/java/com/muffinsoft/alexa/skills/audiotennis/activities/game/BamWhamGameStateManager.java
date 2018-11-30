package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class BamWhamGameStateManager extends OneSideGameStateManager {

    protected static final Logger logger = LogManager.getLogger(BamWhamGameStateManager.class);

    public BamWhamGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.BAM_WHAM;
    }

    @Override
    protected boolean isSuccessAnswer() {
        if (getUserMultipleReplies().isEmpty()) {
            return areEquals(getUserReply(), this.activityProgress.getRequiredUserReaction());
        }
        else {
            for (String reply : getUserMultipleReplies()) {
                if (areEquals(reply, this.activityProgress.getRequiredUserReaction())) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean areEquals(String one, String two) {
        boolean result = one.trim().equalsIgnoreCase(two.trim());
        logger.debug("Compare " + one + " and " + two + ". Are equals: " + result);
        return result;
    }
}