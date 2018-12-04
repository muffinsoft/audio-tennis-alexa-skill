package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.NEW_ACTIVITY_UNLOCKED_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.TRY_SOMETHING_ELSE_PHRASE;

public abstract class TennisGamePhaseStateManager extends TennisBaseGameStateManager {

    protected static final Logger logger = LogManager.getLogger(TennisGamePhaseStateManager.class);

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

        if (checkIfTwoInRow()) {
            handleTwoInRow(builder);
        }
        else if (checkIfThirdTwoInRow()) {
            handleThirdTwoInRow(builder);
        }
        else {
            handleRoundEnd(builder);
        }
        savePersistentAttributes();
        return builder;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {

        iteratePlayerGameCounter(builder);

        if (checkIfTwoInRow()) {
            handleTwoInRow(builder);
        }
        else {
            handleRoundEnd(builder);
        }
        savePersistentAttributes();
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
        initGameStatePhrase(builder);
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

            List<PhraseContainer> dialog = regularPhraseManager.getValueByKey(NEW_ACTIVITY_UNLOCKED_PHRASE);
            List<PhraseContainer> replacesDialog = new ArrayList<>();

            for (PhraseContainer phrase : dialog) {
                String newContent = replaceActivityPlaceholders(phrase.getContent(), aliasManager.getValueByKey(nextActivity.name()));
                BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, phrase.getRole());
                replacesDialog.add(newPhraseContainer);
            }

            builder.addResponse(getDialogTranslator().translate(replacesDialog));
            this.activityProgress.setPossibleActivity(nextActivity);
            this.activityProgress.addUnlockedActivity(nextActivity);
            this.userProgress.addUnlockedActivity(nextActivity);
        }
    }

    private void iteratePlayerGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iteratePlayerGameCounter();
        this.userProgress.iterateWinCounter();
        BasePhraseContainer randomPlayerWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinScore();
        String newContent = replaceScoresPlaceholders(randomPlayerWinScore.getContent(), this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerGameCounter());
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomPlayerWinScore.getRole());
        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
        addGameScores(builder, true);
    }

    private void iterateEnemyGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyGameCounter();
        this.userProgress.iterateLoseCounter();
        BasePhraseContainer randomEnemyWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinScore();
        String newContent = replaceScoresPlaceholders(randomEnemyWinScore.getContent(), this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerGameCounter());
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomEnemyWinScore.getRole());
        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
        addGameScores(builder, false);
    }

    private void addGameScores(DialogItem.Builder builder, boolean isPlayerScores) {
        BasePhraseContainer randomGameScore;
        String newContent;

        if (isPlayerScores) {
            randomGameScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinGame();
        }
        else {
            randomGameScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinGame();
        }

        if (isPlayerScores) {
            newContent = replaceScoresPlaceholders(randomGameScore.getContent(), this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter());
        }
        else {
            newContent = replaceScoresPlaceholders(randomGameScore.getContent(), this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter());
        }

        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomGameScore.getRole());

        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
    }

    private void addPointScores(DialogItem.Builder builder, boolean isPlayerScores) {
        BasePhraseContainer randomTotalScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomTotalScore();
        String newContent;

        if (isPlayerScores) {
            newContent = replaceScoresPlaceholders(randomTotalScore.getContent(), this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter());
        }
        else {
            newContent = replaceScoresPlaceholders(randomTotalScore.getContent(), this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter());
        }

        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomTotalScore.getRole());

        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerWonOnceAtGamePhrase();
        builder.addResponse(getDialogTranslator().translate(phraseContainer));
        this.activityProgress.iteratePlayerPointCounter();
        addPointScores(builder, true);
        appendNextRoundPhrase(builder);
        savePersistentAttributes();
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWonOnce();
        builder.addResponse(getDialogTranslator().translate(randomPhrase));
        this.activityProgress.iterateEnemyPointCounter();
        addPointScores(builder, false);
        appendNextRoundPhrase(builder);
        savePersistentAttributes();
    }

    private String replaceScoresPlaceholders(String inputString, int enemyScores, int scores) {

        if (inputString == null) {
            return null;
        }

        String result = inputString.replace("%scores%", String.valueOf(scores));

        result = result.replace("%enemyScore%", String.valueOf(enemyScores));

        String favour = (enemyScores >= scores) ? "Ben" : "Player";
        result = result.replace("%favour%", String.valueOf(favour));

        return result;
    }

    private String replaceActivityPlaceholders(String inputString, String activity) {
        return inputString.replace("%activity%", activity);
    }

    String replaceWordPlaceholders(String inputString, String word, Character character, String rhyme) {
        if (inputString == null || word == null) {
            return inputString;
        }
        String result = inputString.replace("%word%", word);
        if (character != null) {
            result = result.replace("%letter%", String.valueOf(character));
        }
        if (rhyme != null) {
            result = result.replace("%rhyme%", rhyme);
        }
        return result;
    }

    boolean isWordAlreadyUser() {
        return activityProgress.getUsedWords().contains(getUserReply());
    }
}
