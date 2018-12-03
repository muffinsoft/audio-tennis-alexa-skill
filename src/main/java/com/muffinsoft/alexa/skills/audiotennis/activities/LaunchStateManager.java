package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.UserProgressConverter;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.skills.audiotennis.constants.CardConstants.WELCOME_CARD;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;

public class LaunchStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(LaunchStateManager.class);
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final RegularPhraseManager regularPhraseManager;
    private final CardManager cardManager;

    private Integer userReplyBreakpointPosition;
    private UserProgress userProgress;

    public LaunchStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.cardManager = settingsDependencyContainer.getCardManager();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        this.userProgress = UserProgressConverter.fromJson(String.valueOf(getPersistentAttributes().get(USER_PROGRESS)));
        this.userReplyBreakpointPosition = (Integer) this.getSessionAttributes().getOrDefault(USER_REPLY_BREAKPOINT, null);
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        if (userProgress != null) {
            if (this.userProgress.getAchievements().isEmpty()) {
                buildGreetingWithAwards(builder);
            }
            else {
                buildGreetingWithoutAwards(builder);
            }

            if (this.userProgress.getLosses() > this.userProgress.getWins()) {
                appendEnemyWinsResult(builder);
            }
            else {
                appendPlayerWindResult(builder);
            }

            appendActivitySelection(builder);

            getSessionAttributes().put(SWITCH_ACTIVITY_STEP, true);
            getSessionAttributes().put(INTENT, IntentType.GAME);

            logger.info("Existing user was started new Game Session. Start Royal Greeting");
        }
        else {
            buildInitialGreeting(builder);

            getSessionAttributes().put(INTENT, IntentType.INITIAL_GREETING);

            logger.info("New user was started new Game Session.");
        }

        return builder
                .withCardTitle(cardManager.getValueByKey(WELCOME_CARD))
                .build();
    }

    private void appendActivitySelection(DialogItem.Builder builder) {
        List<PhraseContainer> dialog;
        switch (this.userProgress.getUnlockedActivities().size()) {
            case 1:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_ONE_PHRASE);
                break;
            case 2:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_TWO_PHRASE);
                break;
            case 3:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_THREE_PHRASE);
                break;
            default:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_ALL_PHRASE);
                break;
        }

        builder.addResponse(getDialogTranslator().translate(dialog));
    }

    private void appendEnemyWinsResult(DialogItem.Builder builder) {
        BasePhraseContainer randomEnemyLastScore = this.activitiesPhraseManager.getGreetingsPhrases().getRandomEnemyLastScore();
        String newContent = replaceScoresPlaceholders(randomEnemyLastScore.getContent(), this.userProgress.getLastGameEnemyPoint(), this.userProgress.getLastGamePlayerPoint());
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomEnemyLastScore.getRole());
        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
    }

    private void appendPlayerWindResult(DialogItem.Builder builder) {
        BasePhraseContainer randomPlayerLastScore = this.activitiesPhraseManager.getGreetingsPhrases().getRandomPlayerLastScore();
        String newContent = replaceScoresPlaceholders(randomPlayerLastScore.getContent(), this.userProgress.getLastGamePlayerPoint(), this.userProgress.getLastGameEnemyPoint());
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomPlayerLastScore.getRole());
        builder.addResponse(getDialogTranslator().translate(newPhraseContainer));
    }

    private void buildInitialGreeting(DialogItem.Builder builder) {
        List<BasePhraseContainer> dialog = activitiesPhraseManager.getGreetingsPhrases().getFirstTimeGreeting();

        int index = 0;

        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (this.userReplyBreakpointPosition != null && index <= this.userReplyBreakpointPosition) {
                continue;
            }

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
        }
    }

    private void buildGreetingWithoutAwards(DialogItem.Builder builder) {
        List<BasePhraseContainer> dialog = activitiesPhraseManager.getGreetingsPhrases().getPlayerWithoutAwardsGreeting();
        List<PhraseContainer> newDialog = new ArrayList<>();
        for (BasePhraseContainer phraseContainer : dialog) {
            String newContent = replaceWinAndLosePlaceholders(phraseContainer.getContent(), this.userProgress.getWins(), this.userProgress.getLosses());
            newDialog.add(new BasePhraseContainer(newContent, phraseContainer.getRole()));
        }
        builder.addResponse(getDialogTranslator().translate(newDialog));
    }

    private void buildGreetingWithAwards(DialogItem.Builder builder) {
        List<BasePhraseContainer> dialog = activitiesPhraseManager.getGreetingsPhrases().getPlayerWithAwardsGreeting();
        List<PhraseContainer> newDialog = new ArrayList<>();
        for (BasePhraseContainer phraseContainer : dialog) {
            String newContent = replaceWinAndLosePlaceholders(phraseContainer.getContent(), this.userProgress.getWins(), this.userProgress.getLosses());
            newContent = replaceAwardsPlaceholders(newContent, this.userProgress.getAchievements());
            newDialog.add(new BasePhraseContainer(newContent, phraseContainer.getRole()));
        }
        builder.addResponse(getDialogTranslator().translate(newDialog));
    }

    private String replaceWinAndLosePlaceholders(String inputString, Integer win, Integer lose) {
        inputString = inputString.replace("%wins%", String.valueOf(win));
        inputString = inputString.replace("%losses%", String.valueOf(lose));
        return inputString;
    }

    private String replaceAwardsPlaceholders(String inputString, Set<String> awards) {
        inputString = inputString.replace("%achievements%", String.join(" ", awards));
        return inputString;
    }

    private String replaceScoresPlaceholders(String inputString, Integer scores, Integer emenyScores) {
        inputString = inputString.replace("%scores%", String.valueOf(scores));
        inputString = inputString.replace("%enemyScores%", String.valueOf(emenyScores));
        return inputString;
    }

}
