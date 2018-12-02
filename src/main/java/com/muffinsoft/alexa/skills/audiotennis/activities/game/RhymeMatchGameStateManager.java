package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.CharacterIntersectionCalculator;
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

            if (CharacterIntersectionCalculator.calculate(neededRhyme, repliesRhyme) <= 50f) {
                return false;
            }

            return !isWordAlreadyUser();
        }
        return false;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        BasePhraseContainer playerLosePhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerLoseWrongWordPhrase();

        String newContent = replaceWordPlaceholders(playerLosePhrase.getContent(), getUserReply(), null, this.activityProgress.getPreviousWord());
        playerLosePhrase.setContent(newContent);

        builder.addResponse(getDialogTranslator().translate(playerLosePhrase));

        this.activityProgress.iterateMistakeCount();

        if (this.activityProgress.getMistakeCount() >= settingsForActivity.getAvailableLives()) {
            iterateEnemyScoreCounter(builder);
        }

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType);

        builder.addResponse(getDialogTranslator().translate(nextWord.getWord()));

        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());

        return builder.withSlotName(actionSlotName);
    }
}
