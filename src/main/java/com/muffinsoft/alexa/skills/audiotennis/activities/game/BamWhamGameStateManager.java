package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.SpeechType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.Speech;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BamWhamGameStateManager extends OneSideGameStateManager {

    protected static final Logger logger = LogManager.getLogger(BamWhamGameStateManager.class);

    public BamWhamGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.BAM_WHAM;
    }

    @Override
    protected void appendDynamicEntities(DialogItem.Builder builder) {
        if (this.activityProgress.getRequiredUserReaction() != null && !this.activityProgress.getRequiredUserReaction().isEmpty()) {
            builder.addDynamicEntity(this.activityProgress.getRequiredUserReaction());
        }
    }

    @Override
    protected boolean isIntercepted() {
        return false;
    }

    @Override
    protected boolean isSuccessAnswer() {
        logger.info(">>>> try compare: '" + getActionUserReply() + "' and '" + this.activityProgress.getRequiredUserReaction() + "'");
        return areEquals(getActionUserReply(), this.activityProgress.getRequiredUserReaction());
    }

    private boolean areEquals(String one, String two) {
        if (one == null || two == null) {
            return false;
        }
        if (one.trim().equalsIgnoreCase(two.trim())) {
            return true;
        }
        else {
            List<String> oneList = Arrays.asList(one.split(" "));
            List<String> twoList = Arrays.asList(one.split(" "));
            long intersection = oneList.stream()
                    .distinct()
                    .filter(twoList::contains)
                    .count();
            return intersection >= twoList.size();
        }
    }

    @Override
    Speech getAudioForWord(String word) {
        String path = "https://dzvy8lu2f5aei.cloudfront.net/variables/" + word + ".mp3";
        logger.info("Try to get sound by url " + path);
        return new Speech(SpeechType.AUDIO, path, 0);
    }
}