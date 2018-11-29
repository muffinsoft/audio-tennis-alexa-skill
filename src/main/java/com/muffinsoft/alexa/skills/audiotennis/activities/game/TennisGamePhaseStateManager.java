package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;

public abstract class TennisGamePhaseStateManager extends TennisBaseGameStateManager {

    TennisGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {

        this.activityProgress.iteratePlayerWinRoundCounter();

        if (this.activityProgress.getPlayerRoundWinInRow() == 2) {
            handleTwoInRow(builder);
        }
        else {
            handleWinInRound(builder);
        }
        return builder;
    }

    @Override
    protected DialogItem.Builder handleLoseAnswerOfActivity(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyWinRoundCounter();
        BasePhraseContainer randomPhrase;
        if (this.activityProgress.getEnemyWinRoundCounter() % 2 == 0) {
            randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerLoseTwice();
        }
        else {
            randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWonOnce();
        }

        builder.addResponse(getDialogTranslator().translate(randomPhrase));

        String scores = "Ben Total Scores " + this.activityProgress.getEnemyWinRoundCounter() + ", Your Total Scores " + this.activityProgress.getPlayerRoundWinInRow() + ".";

        builder.addResponse(getDialogTranslator().translate(scores));
        return builder;
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerScoreCounter();
        builder.addResponse(getDialogTranslator().translate(". Wrong word! Score goes to Player."));
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyScoreCounter();
        builder.addResponse(getDialogTranslator().translate(". Score goes to Ben."));
    }

    private void handleTwoInRow(DialogItem.Builder builder) {

        ActivityType nextActivity = progressManager.getNextActivity(this.currentActivityType);
        if (nextActivity != null) {
            this.getSessionAttributes().put(SessionConstants.SWITCH_ACTIVITY_STEP, true);
            builder.addResponse(getDialogTranslator().translate("You have just unlocked next activity. Would you like to try it?"));
            this.activityProgress.setPossibleActivity(nextActivity);
            this.activityProgress.addUnlockedActivity(nextActivity);
        }
    }

    private void handleWinInRound(DialogItem.Builder builder) {
        BasePhraseContainer randomPhrase;
        if (this.activityProgress.getPlayerWinRoundCounter() % 2 == 0) {
            randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWonOnceAtFirstOrThirdAct();
        }
        else {
            randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWonOnceAtSecondOrFourthAct();
        }

        builder.addResponse(getDialogTranslator().translate(randomPhrase));

        String scores = "Ben Total Scores " + this.activityProgress.getEnemyWinRoundCounter() + ", Your Total Scores " + this.activityProgress.getPlayerRoundWinInRow();

        builder.addResponse(getDialogTranslator().translate(scores));

        String restart = "Do you want to restart?";

        this.stateType = StateType.RESTART;
        builder.addResponse(getDialogTranslator().translate(restart));
    }
}
