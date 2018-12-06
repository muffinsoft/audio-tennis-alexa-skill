package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Map;

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

            if (neededRhyme == null) {
                return false;
            }

            if (!neededRhyme.equalsIgnoreCase(repliesRhyme)) {
                return false;
            }

            return !isWordAlreadyUser();
        }
        return false;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        BasePhraseContainer playerLosePhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerLoseWrongWordPhrase();

        builder.addResponse(getDialogTranslator().translate(replaceWordPlaceholders(playerLosePhrase, getUserReply(), null, this.activityProgress.getPreviousWord())));

        this.activityProgress.iterateMistakeCount();

        if (this.activityProgress.getMistakeCount() >= settingsForActivity.getAvailableLives()) {
            iterateEnemyScoreCounter(builder);
        }

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);
        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());

        switch (getUnlockingStatus()) {
            case UNLOCKED:
                handleEnterNewActivity(builder);
                break;
            case CONTINUE:
                handlerContinueRePrompt(builder);
                break;
            case PROCEED:
//                appendNextRoundPhrase(builder);
                builder.addResponse(getDialogTranslator().translate(nextWord.getWord(), enemyRole));
                break;
        }

        return builder.withSlotName(actionSlotName);
    }
}
