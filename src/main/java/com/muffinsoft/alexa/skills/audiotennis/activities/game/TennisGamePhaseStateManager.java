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
        return this.activityProgress.getPlayerPointCounter() == settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected boolean isEndLoseActivityState() {
        return this.activityProgress.getEnemyPointCounter() == settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected DialogItem.Builder handleLoseAnswerOfActivity(DialogItem.Builder builder) {

        iterateEnemyGameCounter(builder);

        BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWonOnce();
        builder.addResponse(getDialogTranslator().translate(randomPhrase));

        if (checkIfTwoInRow()) {
            handleTwoInRow(builder);
        }
        else {
            handleRoundEnd(builder);
        }
        return builder;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {

        iteratePlayerGameCounter(builder);

        BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWonOnceAtFirstOrThirdAct();
        builder.addResponse(getDialogTranslator().translate(randomPhrase));

        if (checkIfTwoInRow()) {
            handleTwoInRow(builder);
        }
        else {
            handleRoundEnd(builder);
        }
        return builder;
    }

    private boolean checkIfTwoInRow() {
        if (this.activityProgress.getEnemyGameWinInRow() != 0) {
            return this.activityProgress.getEnemyGameWinInRow() % 2 == 0;
        }
        else if (this.activityProgress.getPlayerGameWinInRow() != 0) {
            return this.activityProgress.getPlayerGameWinInRow() % 2 == 0;
        }
        else {
            return false;
        }
    }

    private void handleRoundEnd(DialogItem.Builder builder) {

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

    private void iteratePlayerGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerGameCounter();
        addGameScores(builder);
    }

    private void iterateEnemyGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyGameCounter();
        addGameScores(builder);
    }

    private void addGameScores(DialogItem.Builder builder) {
        String scores = "Game win: Ben - " + this.activityProgress.getEnemyGameCounter() + ", Player - " + this.activityProgress.getPlayerGameCounter() + ".";
        builder.addResponse(getDialogTranslator().translate(scores));
    }

    private void addPointScores(DialogItem.Builder builder) {
        String scores = "Game points: Ben - " + this.activityProgress.getEnemyPointCounter() + ", Player " + this.activityProgress.getPlayerPointCounter() + ".";
        builder.addResponse(getDialogTranslator().translate(scores));
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerPointCounter();
        builder.addResponse(getDialogTranslator().translate("Point goes to Player."));
        addPointScores(builder);
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyPointCounter();
        builder.addResponse(getDialogTranslator().translate("Point goes to Ben."));
        addPointScores(builder);
    }

    boolean isWordAlreadyUser() {
        return activityProgress.getUsedWords().contains(getUserReply());
    }
}
