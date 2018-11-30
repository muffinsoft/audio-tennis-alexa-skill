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
    protected boolean isEndWinActivityState() {
        return this.activityProgress.getPlayerScoreCounter() == settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected boolean isEndLoseActivityState() {
        return this.activityProgress.getEnemyScoreCounter() == settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected DialogItem.Builder handleLoseAnswerOfActivity(DialogItem.Builder builder) {

        iterateEnemyWinRoundCounter(builder);

        if (checkIfTwoInRow()) {

            handleTwoInRow(builder);

            return builder;
        }
        else {

            BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWonOnce();

            builder.replaceResponse(getDialogTranslator().translate(randomPhrase));

            handleRoundEnd(builder);

            return builder;
        }
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {

        this.activityProgress.iteratePlayerWinRoundCounter();

        if (checkIfTwoInRow()) {

            handleTwoInRow(builder);

            return builder;
        }
        else {
            BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWonOnceAtFirstOrThirdAct();

            builder.replaceResponse(getDialogTranslator().translate(randomPhrase));

            handleRoundEnd(builder);

            return builder;
        }
    }

    private boolean checkIfTwoInRow() {
        if (this.activityProgress.getEnemyRoundWinInRow() != 0) {
            return this.activityProgress.getEnemyRoundWinInRow() % 2 == 0;
        }
        else if (this.activityProgress.getPlayerRoundWinInRow() != 0) {
            return this.activityProgress.getPlayerRoundWinInRow() % 2 == 0;
        }
        else {
            return false;
        }
    }

    private void handleRoundEnd(DialogItem.Builder builder) {

        String scores = "Ben Total Scores " + this.activityProgress.getEnemyWinRoundCounter() + ", Your Total Scores " + this.activityProgress.getPlayerRoundWinInRow();

        builder.addResponse(getDialogTranslator().translate(scores));

        String restart = "Do you want to restart?";

        this.stateType = StateType.RESTART;

        builder.addResponse(getDialogTranslator().translate(restart));
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

    void iterateEnemyWinRoundCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyWinRoundCounter();
        String scores = "Ben Total Scores " + this.activityProgress.getEnemyWinRoundCounter() + ", Your Total Scores " + this.activityProgress.getPlayerRoundWinInRow() + ".";
        builder.addResponseToBegining(getDialogTranslator().translate(scores));
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerScoreCounter();
        builder.addResponse(getDialogTranslator().translate("Score goes to Player."));
        String scores = "Ben Round Scores " + this.activityProgress.getEnemyScoreCounter() + ", Your Round Scores " + this.activityProgress.getPlayerScoreCounter();
        builder.addResponse(getDialogTranslator().translate(scores));
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyScoreCounter();
        builder.addResponse(getDialogTranslator().translate("Score goes to Ben."));
        String scores = "Ben Round Scores " + this.activityProgress.getEnemyScoreCounter() + ", Your Round Scores " + this.activityProgress.getPlayerScoreCounter();
        builder.addResponse(getDialogTranslator().translate(scores));
    }
}
