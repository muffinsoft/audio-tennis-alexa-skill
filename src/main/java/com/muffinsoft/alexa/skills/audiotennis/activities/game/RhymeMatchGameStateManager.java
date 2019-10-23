package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singleton;

public class RhymeMatchGameStateManager extends OneSideGameStateManager {

    public RhymeMatchGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.RHYME_MATCH;
        this.activityProgress.setCurrentActivity(ActivityType.RHYME_MATCH);
    }

    @Override
    protected void appendDynamicEntities(DialogItem.Builder builder) {

    }

    @Override
    protected boolean isSuccessAnswer() {

        List<String> userReply = getUserReply(SlotName.ACTION);

        for (String word : userReply) {

            String repliesRhyme = activityManager.findRhymeForWord(word);

            if (repliesRhyme == null) {
                continue;
            }

            String neededRhyme = activityProgress.getRequiredUserReaction();

            if (neededRhyme == null) {
                continue;
            }

            if (!neededRhyme.equalsIgnoreCase(repliesRhyme)) {
                continue;
            }

            if (!isWordAlreadyUsed()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected DialogItem.Builder handleMistakeAnswer(DialogItem.Builder builder) {

        List<String> words = Arrays.asList(this.activityProgress.getPreviousWord().split(" "));
        List<String> userReply = getUserReply(SlotName.ACTION);
        if (words.containsAll(userReply)) {
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey("sameWordRhyme"), true));
        } else {
            BasePhraseContainer playerLosePhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerLoseWrongWordPhrase();
            builder.addResponse(getDialogTranslator().translate(replaceWordPlaceholders(playerLosePhrase, getActionUserReply(), null, this.activityProgress.getPreviousWord()), true));
        }

        this.activityProgress.iterateMistakeCount();

        if (this.activityProgress.getMistakeCount() >= settingsForActivity.getAvailableLives()) {
            iterateEnemyScoreCounter(builder);
        }

        WordContainer nextWord = activityManager.getRandomWordForActivity(this.currentActivityType, singleton(this.activityProgress.getPreviousWord()));
        this.activityProgress.setPreviousWord(nextWord.getWord());
        this.activityProgress.setRequiredUserReaction(nextWord.getUserReaction());
        this.activityProgress.addUsedWord(nextWord.getWord());

        switch (getUnlockingStatus()) {
            case UNLOCKED:
                handleEnterNewActivity(builder);
                break;
            case CONTINUE:
                handlerContinueRePrompt(builder);
                break;
            case PROCEED:
                builder.addResponse(getAudioForWord(nextWord.getWord()));
                break;
        }

        return builder;
    }
}
