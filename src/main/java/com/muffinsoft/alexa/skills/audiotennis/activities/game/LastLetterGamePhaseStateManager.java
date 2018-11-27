package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Map;
import java.util.Objects;

public class LastLetterGamePhaseStateManager extends TennisGamePhaseStateManager {

    public LastLetterGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
    }

    @Override
    protected boolean isSuccessAnswer() {

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord();
            char lastLetter = previousWord.charAt(previousWord.length() - 1);

            String userReply = getUserReply();
            char firstLetter = userReply.charAt(0);

            if (!Objects.equals(lastLetter, firstLetter)) {
                return false;
            }

            return !activityProgress.getUsedWords().contains(getUserReply());
        }
        return false;
    }

    @Override
    protected DialogItem.Builder handleSuccessAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateSuccessCounter();

        WordContainer nextWord = getNextRightWordForActivity();
        this.activityProgress.setPreviousWord(nextWord.getWord());

        builder.addResponse(getDialogTranslator().translate(nextWord.getWord()));

        return builder;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        this.activityProgress.iterateMistakeCounter();

        if (this.activityProgress.getMistakeCounter() >= 0) {
            this.stateType = StateType.LOSE;
        }

        WordContainer nextWord = getNextRightWordForActivity();
        this.activityProgress.setPreviousWord(nextWord.getWord());

        builder.addResponse(getDialogTranslator().translate(nextWord.getWord()));

        return builder;
    }

    @Override
    protected boolean isEndWinActivityState() {
        return super.isEndWinActivityState();
    }

    private WordContainer getNextRightWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);

        return activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, lastLetter);
    }

    private WordContainer getNextWrongWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);

        return activityManager.getRandomWordForActivityFromLetter(this.currentActivityType, lastLetter);
    }
}
