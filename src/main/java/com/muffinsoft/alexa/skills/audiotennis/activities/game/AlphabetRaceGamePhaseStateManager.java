package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Map;

public class AlphabetRaceGamePhaseStateManager extends TennisGamePhaseStateManager {

    public AlphabetRaceGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.ALPHABET_RACE;
    }

    @Override
    protected boolean isEndWinActivityState() {
        return this.activityProgress.getSuccessCounter() == settingsForActivity.getScoresToWinRoundCounter();
    }

    @Override
    protected boolean isEndLoseActivityState() {
        return this.activityProgress.getMistakeCount() > settingsForActivity.getAvailableLives();
    }

    @Override
    protected boolean isSuccessAnswer() {
        if (getUserMultipleReplies().isEmpty()) {
            return getUserReply().equals(this.activityProgress.getRequiredUserReaction());
        }
        return false;
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateSuccessCounter();

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);

        builder.addResponse(getDialogTranslator().translate(nextWord.getWord()));

        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());

        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateMistakeCount();

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);

        builder.addResponse(getDialogTranslator().translate("Wrong! "));

        builder.addResponse(getDialogTranslator().translate(nextWord.getWord()));

        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());

        return builder.withSlotName(actionSlotName);
    }
}