package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.model.Speech;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityUnlokingStatus;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.ENEMY_FAVOR_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.FIREWORK_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.NEW_ACTIVITY_UNLOCKED_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.PLAYER_FAVOR_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.TRY_SOMETHING_ELSE_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WANT_RESTART_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.ASK_RANDOM_SWITCH_ACTIVITY_STEP;

public abstract class TennisGamePhaseStateManager extends TennisBaseGameStateManager {

    protected static final Logger logger = LogManager.getLogger(TennisGamePhaseStateManager.class);

    TennisGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
    }

    @Override
    protected boolean isEndWinActivityState() {
        return this.activityProgress.getPlayerPointCounter() >= settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected boolean isEndLoseActivityState() {
        return this.activityProgress.getEnemyPointCounter() >= settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected DialogItem.Builder handleLoseAnswerOfActivity(DialogItem.Builder builder) {
        Speech mistake = builder.popFirstSpeech();
        iterateEnemyGameCounter(builder);
        builder.addResponseToBegining(mistake);
        handleRoundEnd(builder);
        savePersistentAttributes();
        return builder;
    }

    @Override
    protected DialogItem.Builder handleWinAnswerOfActivity(DialogItem.Builder builder) {
        Speech wrongWord = null;
        Speech mistakeDescription = null;
        if (builder.getSpeechSize() >= 2) {
            wrongWord = builder.popFirstSpeech();
            mistakeDescription = builder.popFirstSpeech();
        }
        iteratePlayerGameCounter(builder);
        if (builder.getSpeechSize() >= 2) {
            if (mistakeDescription != null) {
                builder.addResponseToBegining(mistakeDescription);
            }
            if (wrongWord != null) {
                builder.addResponseToBegining(wrongWord);
            }
        }
        handleRoundEnd(builder);
        savePersistentAttributes();
        return builder;
    }

    ActivityUnlokingStatus getUnlockingStatus() {
        if (checkIfTwoInRow()) {
            this.activityProgress.iterateAmountOfTwoPointInRow();
            this.userProgress.setAmountOfTwoPointsInRow(this.activityProgress.getAmountOfTwoPointsInRow());
            savePersistentAttributes();
            int iterationValue = this.activityProgress.getAmountOfTwoPointsInRow() - 1;
            ActivityType nextActivity = progressManager.getNextActivity(this.activityProgress.getUnlockedActivities());
            if (iterationValue == 0) {
                if (nextActivity != null) {
                    return ActivityUnlokingStatus.UNLOCKED;
                }
                else {
                    return ActivityUnlokingStatus.CONTINUE;
                }
            }
            else if (iterationValue % 3 == 0) {
                if (nextActivity != null) {
                    return ActivityUnlokingStatus.UNLOCKED;
                }
                else {
                    return ActivityUnlokingStatus.CONTINUE;
                }
            }
            else {
                return ActivityUnlokingStatus.CONTINUE;
            }
        }
        else {
            return ActivityUnlokingStatus.PROCEED;
        }
    }

    private boolean checkIfTwoInRow() {
        boolean result = false;
        if (this.activityProgress.getEnemyPointWinInRow() != 0) {
            result = this.activityProgress.getEnemyPointWinInRow() % 2 == 0;
        }
        else if (this.activityProgress.getPlayerPointWinInRow() != 0) {
            result = this.activityProgress.getPlayerPointWinInRow() % 2 == 0;
        }
        return result;
    }

    private void handleRoundEnd(DialogItem.Builder builder) {

        this.stateType = StateType.RESTART;

        this.userProgress.setEndRound(true);

        getSessionAttributes().remove(ASK_RANDOM_SWITCH_ACTIVITY_STEP);

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(WANT_RESTART_PHRASE)));
    }

    void handlerContinueRePrompt(DialogItem.Builder builder) {

        getSessionAttributes().put(ASK_RANDOM_SWITCH_ACTIVITY_STEP, true);

        List<PhraseContainer> valueByKey = regularPhraseManager.getValueByKey(TRY_SOMETHING_ELSE_PHRASE);
        builder.addResponse(getDialogTranslator().translate(valueByKey));
    }

    void handleEnterNewActivity(DialogItem.Builder builder) {

        if (!this.activityProgress.getUnlockedActivities().contains(this.currentActivityType)) {
            this.activityProgress.addUnlockedActivity(this.currentActivityType);
        }

        ActivityType nextActivity = progressManager.getNextActivity(this.activityProgress.getUnlockedActivities());

        if (nextActivity != null) {

            this.getSessionAttributes().put(SessionConstants.SWITCH_ACTIVITY_STEP, true);

            List<PhraseContainer> dialog = regularPhraseManager.getValueByKey(NEW_ACTIVITY_UNLOCKED_PHRASE);
            List<PhraseContainer> replacesDialog = new ArrayList<>();

            for (PhraseContainer phrase : dialog) {
                replacesDialog.add(replaceActivityPlaceholders(phrase, aliasManager.getValueByKey(nextActivity.name())));
            }

            builder.addResponse(getDialogTranslator().translate(replacesDialog));
            this.activityProgress.setCurrentActivity(this.currentActivityType);
            this.activityProgress.setPossibleActivity(nextActivity);
            this.activityProgress.addUnlockedActivity(this.currentActivityType);
            this.activityProgress.addUnlockedActivity(nextActivity);
            this.userProgress.addUnlockedActivity(nextActivity);
            savePersistentAttributes();
        }
        else {
            logger.error("Activity progress before error " + this.activityProgress);
            throw new IllegalArgumentException("Can't create unlocking dialog without next activity");
        }
    }

    private void iteratePlayerGameCounter(DialogItem.Builder builder) {

        this.activityProgress.iteratePlayerGameCounter();

        this.userProgress.iterateWinCounter();

        BasePhraseContainer randomVictoryPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomVictoryPhrase();
        builder.replaceResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomVictoryPhrase, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false)));

        BasePhraseContainer randomPlayerWinGame = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinGame();
        builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomPlayerWinGame, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false)));

        BasePhraseContainer randomPlayerWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinScore();
        builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomPlayerWinScore, this.activityProgress.getEnemyGameCounter(), this.activityProgress.getPlayerGameCounter(), false)));

        BasePhraseContainer randomCallToCelebrate = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomCallToCelebrate();
        builder.addResponse(getDialogTranslator().translate(randomCallToCelebrate));

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(FIREWORK_PHRASE)));

        if (activityProgress.isTimeToLevelUp(settingsForActivity)) {
            activityProgress.updateWithDifficultSettings(settingsForActivity);
            BasePhraseContainer randomLevelUps = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomLevelUps();
            builder.addResponse(getDialogTranslator().translate(randomLevelUps));
        }

        if (activityProgress.isTimeToAddNickName()) {
            activityProgress.iterateNickNameCounter();
            userProgress.setNickNameLevel(activityProgress.getCurrentNickNameLevel());
            String nextNickName = progressManager.findNextNickName(this.activityProgress.getCurrentNickNameLevel());
            BasePhraseContainer randomPromotions = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPromotions();
            builder.addResponse(getDialogTranslator().translate(replaceNickName(randomPromotions, nextNickName)));
        }
    }

    private void iterateEnemyGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyGameCounter();
        this.userProgress.iterateLoseCounter();

        BasePhraseContainer randomEnemyWinGame = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinGame();
        builder.replaceResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomEnemyWinGame, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false)));

        BasePhraseContainer randomDefeatPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomDefeatPhrase();
        builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomDefeatPhrase, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false)));
    }

    private void addPointScores(DialogItem.Builder builder, boolean isPlayerScores) {
        BasePhraseContainer randomTotalScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomTotalScore();
        BasePhraseContainer newPhraseContainer;

        if (isPlayerScores) {
            newPhraseContainer = replaceScoresPlaceholders(randomTotalScore, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), true);
        }
        else {
            newPhraseContainer = replaceScoresPlaceholders(randomTotalScore, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), true);
        }

        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerWonOnceAtGamePhrase();
        builder.addResponse(getDialogTranslator().translate(phraseContainer));
        this.activityProgress.iteratePlayerPointCounter();
        this.userProgress.setPlayerPointWinInRow(this.activityProgress.getPlayerPointWinInRow());
        addPointScores(builder, true);
        savePersistentAttributes();
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWonOnce();
        builder.addResponse(getDialogTranslator().translate(randomPhrase));
        this.activityProgress.iterateEnemyPointCounter();
        this.userProgress.setEnemyPointWinInRow(this.activityProgress.getEnemyPointWinInRow());
        addPointScores(builder, false);
        savePersistentAttributes();
    }

    private BasePhraseContainer replaceNickName(BasePhraseContainer inputContainer, String rookie) {
        String newContent = inputContainer.getContent().replace("%rookie%", rookie);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private BasePhraseContainer replaceScoresPlaceholders(BasePhraseContainer inputContainer, int enemyScores, int scores, boolean withFavor) {
        String newContent = replaceScoresPlaceholders(inputContainer.getContent(), enemyScores, scores, withFavor);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private String replaceScoresPlaceholders(String inputString, int enemyScores, int scores, boolean withFavor) {

        String result = inputString.replace("%scores%", String.valueOf(scores));
        result = result.replace("%enemyScore%", String.valueOf(enemyScores));

        if (withFavor) {
            if (enemyScores > scores) {
                result += regularPhraseManager.getValueByKey(ENEMY_FAVOR_PHRASE).get(0).getContent();
            }
            else if (scores > enemyScores) {
                result += regularPhraseManager.getValueByKey(PLAYER_FAVOR_PHRASE).get(0).getContent();
            }
        }

        return result;
    }

    private BasePhraseContainer replaceActivityPlaceholders(PhraseContainer inputContainer, String activity) {
        String newContent = replaceActivityPlaceholders(inputContainer.getContent(), activity);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private String replaceActivityPlaceholders(String inputString, String activity) {
        return inputString.replace("%activity%", activity);
    }

    BasePhraseContainer replaceCharacterPlaceholders(PhraseContainer inputContainer, Character character) {
        String newContent = replaceCharacterPlaceholders(inputContainer.getContent(), character);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private String replaceCharacterPlaceholders(String inputString, Character character) {
        return inputString.replace("%letter%", String.valueOf(character));
    }

    BasePhraseContainer replaceWordPlaceholders(PhraseContainer inputContainer, String word, Character character, String rhyme) {
        String newContent = replaceWordPlaceholders(inputContainer.getContent(), word, character, rhyme);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private String replaceWordPlaceholders(String inputString, String word, Character character, String rhyme) {
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
