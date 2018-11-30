package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;
import java.util.Objects;

public class RhymeMatchGameStateManager extends OneSideGameStateManager {

    public RhymeMatchGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.RHYME_MATCH;
    }

    @Override
    protected boolean isSuccessAnswer() {

        if (getUserMultipleReplies().isEmpty()) {

            String userReply = getUserReply();

            String repliesRhyme = activityManager.findRhymeForWord(userReply);

            if (repliesRhyme == null) {
                return false;
            }

            String neededRhyme = activityProgress.getRequiredUserReaction();

            if (!Objects.equals(repliesRhyme, neededRhyme)) {
                return false;
            }

            return !isWordAlreadyUser();
        }
        return false;
    }
}
