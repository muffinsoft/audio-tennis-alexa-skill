package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.NEW_ACTIVITY_UNLOCKED_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.TRY_SOMETHING_ELSE_PHRASE;

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
        else if (checkIfThirdTwoInRow()) {
            handleThirdTwoInRow(builder);
        }
        else {
            handleRoundEnd(builder);
        }
        return builder;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {

        iteratePlayerGameCounter(builder);

        boolean isTwoInRow = checkIfTwoInRow();

        BasePhraseContainer randomPhrase;

        if (isTwoInRow) {
            randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWonTwice();
        }
        else {
            randomPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerWonOnceAtGamePhrase();
        }

        builder.addResponse(getDialogTranslator().translate(randomPhrase));

        if (isTwoInRow) {
            handleTwoInRow(builder);
        }
        else {
            handleRoundEnd(builder);
        }
        return builder;
    }

    private boolean checkIfThirdTwoInRow() {
        return this.activityProgress.getAmountOfGameInRow() != 0 && this.activityProgress.getAmountOfGameInRow() % 3 == 0;
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

    private void handleThirdTwoInRow(DialogItem.Builder builder) {

        this.getSessionAttributes().put(SessionConstants.RANDOM_SWITCH_ACTIVITY_STEP, true);
        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(TRY_SOMETHING_ELSE_PHRASE)));
    }

    private void handleTwoInRow(DialogItem.Builder builder) {

        this.activityProgress.iterateAmountOfGameInRow();

        ActivityType nextActivity = progressManager.getNextActivity(this.currentActivityType);
        if (nextActivity != null) {
            this.getSessionAttributes().put(SessionConstants.SWITCH_ACTIVITY_STEP, true);

            List<PhraseContainer> valueByKey = regularPhraseManager.getValueByKey(NEW_ACTIVITY_UNLOCKED_PHRASE);
            for (PhraseContainer phrase : valueByKey) {
                String newContent = replaceActivityPlaceholders(phrase.getContent(), aliasManager.getValueByKey(nextActivity.name()));
                ((BasePhraseContainer) phrase).setContent(newContent);
            }

            builder.addResponse(getDialogTranslator().translate(valueByKey));
            this.activityProgress.setPossibleActivity(nextActivity);
            this.activityProgress.addUnlockedActivity(nextActivity);
        }
    }

    private void iteratePlayerGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerGameCounter();
        BasePhraseContainer randomPlayerWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinScore();
        String newContent = replaceScoresPlaceholders(randomPlayerWinScore.getContent(), this.activityProgress.getPlayerGameCounter());
        randomPlayerWinScore.setContent(newContent);
        builder.addResponse(getDialogTranslator().translate(randomPlayerWinScore));
        addGameScores(builder, true);
    }

    private void iterateEnemyGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyGameCounter();
        BasePhraseContainer randomEnemyWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinScore();
        String newContent = replaceScoresPlaceholders(randomEnemyWinScore.getContent(), this.activityProgress.getPlayerGameCounter());
        randomEnemyWinScore.setContent(newContent);
        builder.addResponse(getDialogTranslator().translate(randomEnemyWinScore));
        addGameScores(builder, false);
    }

    private void addGameScores(DialogItem.Builder builder, boolean isPlayerScores) {
        BasePhraseContainer randomGameScore;
        String newContent;
        if (isPlayerScores) {
            randomGameScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinGame();
            newContent = replaceScoresPlaceholders(randomGameScore.getContent(), this.activityProgress.getPlayerGameCounter());
        }
        else {
            randomGameScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinGame();
            newContent = replaceScoresPlaceholders(randomGameScore.getContent(), this.activityProgress.getEnemyGameCounter());
        }

        randomGameScore.setContent(newContent);

        builder.addResponse(getDialogTranslator().translate(randomGameScore));
    }

    private void addPointScores(DialogItem.Builder builder, boolean isPlayerScores) {
        BasePhraseContainer randomTotalScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomTotalScore();
        String newContent;
        if (isPlayerScores) {
            newContent = replaceScoresPlaceholders(randomTotalScore.getContent(), this.activityProgress.getPlayerPointCounter(), "Player", null);
        }
        else {
            newContent = replaceScoresPlaceholders(randomTotalScore.getContent(), this.activityProgress.getEnemyPointCounter(), "Ben", null);
        }
        randomTotalScore.setContent(newContent);
        builder.addResponse(getDialogTranslator().translate(randomTotalScore));
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerPointCounter();
        addPointScores(builder, true);
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyPointCounter();
        addPointScores(builder, false);
    }

    private String replaceScoresPlaceholders(String inputString, Integer scores) {
        return replaceScoresPlaceholders(inputString, scores, null, null);
    }

    private String replaceScoresPlaceholders(String inputString, Integer scores, String favour, Integer enemyScores) {
        if (scores != null) {
            inputString = inputString.replace("%scores%", String.valueOf(scores));
        }
        if (enemyScores != null) {
            inputString = inputString.replace("%enemyScore%", String.valueOf(enemyScores));
        }
        if (favour != null) {
            inputString = inputString.replace("%favour%", String.valueOf(favour));
        }
        return inputString;
    }

    String replaceActivityPlaceholders(String inputString, String activity) {
        if (activity != null) {
            inputString = inputString.replace("%activity%", activity);
        }
        return inputString;
    }

    String replaceWordPlaceholders(String inputString, String word, Character character, String rhyme) {
        if (word != null) {
            inputString = inputString.replace("%word%", word);
        }
        if (character != null) {
            inputString = inputString.replace("%letter%", String.valueOf(character));
        }
        if (rhyme != null) {
            inputString = inputString.replace("%rhyme%", rhyme);
        }
        return inputString;
    }

    boolean isWordAlreadyUser() {
        return activityProgress.getUsedWords().contains(getUserReply());
    }
}
