package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.model.Speech;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityUnlokingStatus;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.components.NumberTranslator.translateToOrdinalValue;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.ENEMY_FAVOR_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.FIREWORK_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.NEW_ACTIVITY_UNLOCKED_PHRASE_B;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.NEW_ACTIVITY_UNLOCKED_PHRASE_C;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.NEW_ACTIVITY_UNLOCKED_PHRASE_D;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.PLAYER_FAVOR_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.TRY_SOMETHING_ELSE_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WANT_RESTART_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.ASK_RANDOM_SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_UNLOCK_ACTIVITY_STEP;

public abstract class TennisGamePhaseStateManager extends TennisBaseGameStateManager {

    protected static final Logger logger = LogManager.getLogger(TennisGamePhaseStateManager.class);

    TennisGamePhaseStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
    }

    @Override
    protected boolean isEndWinActivityState() {
        return this.activityProgress.getPlayerPointCounter() > settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected boolean isEndLoseActivityState() {
        return this.activityProgress.getEnemyPointCounter() > settingsForActivity.getScoresToWinRoundValue();
    }

    @Override
    protected DialogItem.Builder handleLoseAnswerOfActivity(DialogItem.Builder builder) {
        Speech mistake = builder.popFirstSpeech();
        iterateEnemyGameCounter(builder);
        builder.withAplDocument(aplManager.getScoreDocument());
        builder.withAplTemplateData(generateAplTemplatePointData());
        builder.removeAllBackgroundImageUrls();
        builder.addBackgroundImageUrl(cardManager.getValueByKey("point-score"));
        builder.addBackgroundImageUrl(settingsForActivity.getIntroImage());
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
            builder.removeAllSpeeches();
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
        builder.withAplDocument(aplManager.getScoreDocument());
        builder.withAplTemplateData(generateAplTemplatePointData());
        builder.removeAllBackgroundImageUrls();
        builder.addBackgroundImageUrl(cardManager.getValueByKey("point-win-score"));
        builder.addBackgroundImageUrl(settingsForActivity.getIntroImage());
        handleRoundEnd(builder);
        savePersistentAttributes();
        return builder;
    }

    private Map<String, String> generateAplTemplatePointData() {
        Map<String, String> data = new HashMap<>();
        data.put("playerScore", String.valueOf(this.activityProgress.getPlayerPointCounter()));
        data.put("enemyScore", String.valueOf(this.activityProgress.getEnemyPointCounter()));
        return data;
    }

    ActivityUnlokingStatus getUnlockingStatus() {
        if (checkIfTwoInRow()) {
            this.activityProgress.iterateAmountOfTwoPointInRow();
            this.userProgress.setAmountOfTwoPointsInRow(this.activityProgress.getAmountOfTwoPointsInRow());
            this.userProgress.setEnemyPointWinInRow(this.activityProgress.getEnemyPointWinInRow());
            this.userProgress.setPlayerPointWinInRow(this.activityProgress.getPlayerPointWinInRow());
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

        this.userProgress.resetRound();

        getSessionAttributes().remove(ASK_RANDOM_SWITCH_ACTIVITY_STEP);
        getSessionAttributes().remove(SWITCH_UNLOCK_ACTIVITY_STEP);
        getSessionAttributes().remove(SWITCH_ACTIVITY_STEP);

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(WANT_RESTART_PHRASE), true));
    }

    void handlerContinueRePrompt(DialogItem.Builder builder) {

        getSessionAttributes().remove(SWITCH_ACTIVITY_STEP);
        getSessionAttributes().remove(SWITCH_UNLOCK_ACTIVITY_STEP);
        getSessionAttributes().put(ASK_RANDOM_SWITCH_ACTIVITY_STEP, true);

        List<PhraseContainer> valueByKey = regularPhraseManager.getValueByKey(TRY_SOMETHING_ELSE_PHRASE);
        builder.addResponse(getDialogTranslator().translate(valueByKey, true));
    }

    void handleEnterNewActivity(DialogItem.Builder builder) {

        if (!this.activityProgress.getUnlockedActivities().contains(this.currentActivityType)) {
            this.activityProgress.addUnlockedActivity(this.currentActivityType);
        }

        ActivityType nextActivity = progressManager.getNextActivity(this.activityProgress.getUnlockedActivities());

        if (nextActivity != null) {

            getSessionAttributes().remove(ASK_RANDOM_SWITCH_ACTIVITY_STEP);
            getSessionAttributes().remove(SWITCH_ACTIVITY_STEP);
            getSessionAttributes().put(SWITCH_UNLOCK_ACTIVITY_STEP, true);

            List<PhraseContainer> dialog;

            switch (nextActivity) {
                case BAM_WHAM:
                    dialog = regularPhraseManager.getValueByKey(NEW_ACTIVITY_UNLOCKED_PHRASE_B);
                    break;
                case ALPHABET_RACE:
                    dialog = regularPhraseManager.getValueByKey(NEW_ACTIVITY_UNLOCKED_PHRASE_C);
                    break;
                case RHYME_MATCH:
                    dialog = regularPhraseManager.getValueByKey(NEW_ACTIVITY_UNLOCKED_PHRASE_D);
                    break;
                default:
                    dialog = Collections.emptyList();
            }

            builder.addResponse(getDialogTranslator().translate(dialog, true));
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
        if (randomVictoryPhrase.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomVictoryPhrase, true));
        }
        else {
            builder.replaceResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomVictoryPhrase, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false, false), true));
        }

        BasePhraseContainer randomPlayerWinGame = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinGame();
        if (randomPlayerWinGame.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomPlayerWinGame, true));
        }
        else {
            builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomPlayerWinGame, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false, false), true));
        }

        BasePhraseContainer randomPlayerWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPlayerWinScore();
        if (randomPlayerWinScore.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomPlayerWinScore, true));
        }
        else {
            builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomPlayerWinScore, this.activityProgress.getEnemyGameCounter(), this.activityProgress.getPlayerGameCounter(), false, true), true));
        }

        BasePhraseContainer randomCallToCelebrate = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomCallToCelebrate();
        builder.addResponse(getDialogTranslator().translate(randomCallToCelebrate, true));

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(FIREWORK_PHRASE), true));

        if (activityProgress.isTimeToLevelUp(settingsForActivity)) {
            activityProgress.updateWithDifficultSettings(settingsForActivity);
            BasePhraseContainer randomLevelUps = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomLevelUps();
            builder.addResponse(getDialogTranslator().translate(randomLevelUps, true));
        }

        if (activityProgress.isTimeToAddNickName()) {
            activityProgress.iterateNickNameCounter();
            userProgress.setNickNameLevel(activityProgress.getCurrentNickNameLevel());
            String nextNickName = progressManager.findNextNickName(this.activityProgress.getCurrentNickNameLevel());
            BasePhraseContainer randomPromotions = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomPromotions();
            if (randomPromotions.getRole().equals("Audio")) {
                builder.addResponse(getDialogTranslator().translate(randomPromotions, true));
                builder.addResponse(getDialogTranslator().translate(nextNickName, true));
            }
            else {
                builder.addResponse(getDialogTranslator().translate(replaceNickName(randomPromotions, nextNickName), true));
            }
        }
    }

    private void iterateEnemyGameCounter(DialogItem.Builder builder) {
        this.activityProgress.iterateEnemyGameCounter();
        this.userProgress.iterateLoseCounter();

        BasePhraseContainer randomEnemyWinGame = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinGame();
        if (randomEnemyWinGame.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomEnemyWinGame, true));
        }
        else {
            builder.replaceResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomEnemyWinGame, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false, false), true));
        }

        BasePhraseContainer randomEnemyWinScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWinScore();
        if (randomEnemyWinScore.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomEnemyWinScore, true));
            builder.addResponse(getDialogTranslator().translate(variablesManager.getValueByKey(String.valueOf(this.activityProgress.getEnemyGameCounter())), true));
            String link = randomEnemyWinScore.getAudio();
            String key = link.substring(0, link.indexOf('_'));
            List<BasePhraseContainer> allPhrases = generalActivityPhraseManager.getGeneralActivityPhrases().getAllEnemyWinScoreBySameKey(key);
            allPhrases.remove(randomEnemyWinScore);
            if (!allPhrases.isEmpty()) {
                builder.addResponse(getDialogTranslator().translate(allPhrases.get(0), true));
            }
        }
        else {
            builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomEnemyWinScore, this.activityProgress.getEnemyGameCounter(), this.activityProgress.getPlayerGameCounter(), false, true), true));
        }

        BasePhraseContainer randomDefeatPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomDefeatPhrase();
        if (randomDefeatPhrase.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomDefeatPhrase, true));
        }
        else {
            builder.addResponse(getDialogTranslator().translate(replaceScoresPlaceholders(randomDefeatPhrase, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), false, false), true));
        }
    }

    private void addPointScores(DialogItem.Builder builder, boolean isPlayerScores) {
        BasePhraseContainer randomTotalScore = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomTotalScore();

        if (randomTotalScore.getRole().equals("Audio")) {
            builder.addResponse(getDialogTranslator().translate(randomTotalScore, true));
        }
        else {
            BasePhraseContainer newPhraseContainer;

            if (isPlayerScores) {
                newPhraseContainer = replaceScoresPlaceholders(randomTotalScore, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), true, false);
            }
            else {
                newPhraseContainer = replaceScoresPlaceholders(randomTotalScore, this.activityProgress.getEnemyPointCounter(), this.activityProgress.getPlayerPointCounter(), true, false);
            }
            builder.addResponse(getDialogTranslator().translate(newPhraseContainer, true));
        }
    }

    void iteratePlayerScoreCounter(DialogItem.Builder builder) {
        BasePhraseContainer phraseContainer = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomPlayerWonOnceAtGamePhrase();
        this.activityProgress.iteratePlayerPointCounter();
        builder.addResponse(getDialogTranslator().translate(phraseContainer, true));
        builder.withAplDocument(aplManager.getScoreDocument());
        builder.withAplTemplateData(generateAplTemplatePointData());
        builder.addBackgroundImageUrl(cardManager.getValueByKey("point-score"));
        builder.addBackgroundImageUrl(settingsForActivity.getIntroImage());
        this.userProgress.setPlayerPointWinInRow(this.activityProgress.getPlayerPointWinInRow());
        addPointScores(builder, true);
        savePersistentAttributes();
    }

    void iterateEnemyScoreCounter(DialogItem.Builder builder) {
        BasePhraseContainer randomPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomEnemyWonOnce();
        this.activityProgress.iterateEnemyPointCounter();
        builder.addResponse(getDialogTranslator().translate(randomPhrase, true));
        builder.withAplDocument(aplManager.getScoreDocument());
        builder.withAplTemplateData(generateAplTemplatePointData());
        builder.addBackgroundImageUrl(cardManager.getValueByKey("point-score"));
        builder.addBackgroundImageUrl(settingsForActivity.getIntroImage());
        this.userProgress.setEnemyPointWinInRow(this.activityProgress.getEnemyPointWinInRow());
        addPointScores(builder, false);
        savePersistentAttributes();
    }

    private BasePhraseContainer replaceNickName(BasePhraseContainer inputContainer, String rookie) {
        String newContent = inputContainer.getContent().replace("%rookie%", rookie);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private BasePhraseContainer replaceScoresPlaceholders(BasePhraseContainer inputContainer, int enemyScores, int scores, boolean withFavor, boolean toOrdinals) {
        String newContent = replaceScoresPlaceholders(inputContainer.getContent(), enemyScores, scores, withFavor, toOrdinals);
        return new BasePhraseContainer(newContent, inputContainer.getRole());
    }

    private String replaceScoresPlaceholders(String inputString, int enemyScores, int scores, boolean withFavor, boolean toOrdinals) {
        String result;
        if (toOrdinals) {
            result = inputString.replace("%scores%", translateToOrdinalValue(scores));
            result = result.replace("%enemyScore%", translateToOrdinalValue(enemyScores));
        }
        else {
            result = inputString.replace("%scores%", String.valueOf(scores));
            result = result.replace("%enemyScore%", String.valueOf(enemyScores));
        }

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

    boolean isWordAlreadyUsed() {
        return activityProgress.getUsedWords().contains(getActionUserReply());
    }
}
