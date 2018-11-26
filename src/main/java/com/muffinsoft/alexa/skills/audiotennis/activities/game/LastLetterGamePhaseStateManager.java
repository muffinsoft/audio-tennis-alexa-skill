package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.Map;

public class LastLetterGamePhaseStateManager extends TennisGamePhaseStateManager {

    public LastLetterGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer);
    }

    @Override
    protected boolean isSuccessAnswer() {

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord();
            char lastLetter = previousWord.charAt(previousWord.length() - 1);

            String userReply = getUserReply();
            char firstLetter = userReply.charAt(0);

            return lastLetter == firstLetter;
        }
        return false;
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {
        this.activityProgress.iterateSuccessCounter();

        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);

        String nextWord = activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, lastLetter);

        builder.addResponse(getDialogTranslator().translate(nextWord));

        return builder;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {
        this.activityProgress.iterateMistakeCounter();

        if (this.activityProgress.getMistakeCounter() >= 0) {
            this.stateType = StateType.LOSE;
        }
        generateRandomWord();
        return null;
    }

    @Override
    protected boolean isEndWinActivityState() {
        return super.isEndWinActivityState();
    }
}
