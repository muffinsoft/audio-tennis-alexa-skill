package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.ActivitySelectionAppender;
import com.muffinsoft.alexa.skills.audiotennis.components.UserProgressConverter;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AplManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.*;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SELECT_ACTIVITY_STEP;

public class LaunchStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(LaunchStateManager.class);
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final AplManager aplManager;
    private final CardManager cardManager;
    private final ActivitySelectionAppender activitySelectionAppender;

    private Integer userReplyBreakpointPosition;
    private UserProgress userProgress;

    public LaunchStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
        this.aplManager = settingsDependencyContainer.getAplManager();
        this.cardManager = settingsDependencyContainer.getCardManager();
        this.activitySelectionAppender = settingsDependencyContainer.getActivitySelectionAppender();
    }

    @Override
    protected void populateActivityVariables() {
        String userProgress = String.valueOf(getPersistentAttributes().get(USER_PROGRESS));
        this.userProgress = UserProgressConverter.fromJson(userProgress);
        this.userReplyBreakpointPosition = (Integer) this.getSessionAttributes().getOrDefault(USER_REPLY_BREAKPOINT, null);
        if (this.userProgress.getUnlockedActivities() != null) {
            Set<ActivityType> unlocked = this.userProgress.getUnlockedActivities().stream()
                    .map(ActivityType::valueOf)
                    .collect(Collectors.toSet());
            ActivityProgress activityProgress = new ActivityProgress(null);
            activityProgress.getUnlockedActivities().addAll(unlocked);
            activityProgress.getUnlockedActivities().remove(null);
            this.getSessionAttributes().put(ACTIVITY_PROGRESS, activityProgress);
        }
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        builder
                .withAplDocument(aplManager.getImageDocument())
                .addBackgroundImageUrl(cardManager.getValueByKey("greeting"));

        if (userProgress != null) {

            if (this.userProgress.getAchievements().isEmpty()) {
                buildGreetingWithoutAwards(builder);
            }
            else {
                buildGreetingWithAwards(builder);
            }

            if (this.userProgress.getLastGameHistoryEnemyPoint() != 0 || this.userProgress.getLastGameHistoryPlayerPoint() != 0) {
                if (this.userProgress.getLastGameHistoryEnemyPoint() > this.userProgress.getLastGameHistoryPlayerPoint()) {
                    appendEnemyWinsResult(builder);
                }
                else if(this.userProgress.getLastGameHistoryEnemyPoint() == this.userProgress.getLastGameHistoryPlayerPoint()) {
                    appendDrawResult(builder);
                }
                else {
                    appendPlayerWinsResult(builder);
                }
            }

            boolean withSelection = activitySelectionAppender.appendWithSelection(builder, userProgress, getSessionAttributes());

            if (withSelection) {
                getSessionAttributes().put(SELECT_ACTIVITY_STEP, true);
            }
            getSessionAttributes().put(INTENT, IntentType.GAME);

            logger.debug("Existing user was started new Game Session. Start Royal Greeting");
        }
        else {
            buildInitialGreeting(builder);

            getSessionAttributes().put(INTENT, IntentType.INITIAL_GREETING);

            logger.debug("New user was started new Game Session.");
        }

        return builder.build();
    }

    private void appendEnemyWinsResult(DialogItem.Builder builder) {
        BasePhraseContainer randomEnemyLastScore = this.activitiesPhraseManager.getGreetingsPhrases().getRandomEnemyLastScore();
        replaceScores(builder, randomEnemyLastScore);
    }

    private void appendDrawResult(DialogItem.Builder builder) {
        BasePhraseContainer randomPlayerLastScore = this.activitiesPhraseManager.getGreetingsPhrases().getRandomDrawLastScore();
        replaceScores(builder, randomPlayerLastScore);
    }

    private void appendPlayerWinsResult(DialogItem.Builder builder) {
        BasePhraseContainer randomPlayerLastScore = this.activitiesPhraseManager.getGreetingsPhrases().getRandomPlayerLastScore();
        replaceScores(builder, randomPlayerLastScore);
    }

    private void replaceScores(DialogItem.Builder builder, BasePhraseContainer randomPlayerLastScore) {
        String newContent = replaceScoresPlaceholders(randomPlayerLastScore.getContent(), this.userProgress.getLastGameHistoryPlayerPoint(), this.userProgress.getLastGameHistoryEnemyPoint());
        BasePhraseContainer newPhraseContainer = new BasePhraseContainer(newContent, randomPlayerLastScore.getRole());
        builder.addResponse(getDialogTranslator().translate(newPhraseContainer, true));
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
            builder.addResponse(getDialogTranslator().translate(phraseSettings, true));
        }
    }

    private void buildGreetingWithoutAwards(DialogItem.Builder builder) {
        List<BasePhraseContainer> dialog = activitiesPhraseManager.getGreetingsPhrases().getPlayerWithoutAwardsGreeting();
        List<PhraseContainer> newDialog = new ArrayList<>();
        for (BasePhraseContainer phraseContainer : dialog) {
            if (phraseContainer.getRole().equals("Audio")) {
                newDialog.add(phraseContainer);
            }
            else {
                String newContent = replaceWinAndLosePlaceholders(phraseContainer.getContent(), this.userProgress.getWins(), this.userProgress.getLosses());
                if (!newContent.isEmpty()) {
                    newDialog.add(new BasePhraseContainer(newContent, phraseContainer.getRole()));
                }
            }
        }
        builder.addResponse(getDialogTranslator().translate(newDialog, true));
    }

    private void buildGreetingWithAwards(DialogItem.Builder builder) {
        List<BasePhraseContainer> dialog = activitiesPhraseManager.getGreetingsPhrases().getPlayerWithAwardsGreeting();
        List<PhraseContainer> newDialog = new ArrayList<>();
        for (BasePhraseContainer phraseContainer : dialog) {
            if (phraseContainer.getRole().equals("Audio")) {
                newDialog.add(phraseContainer);
            }
            else {
                String newContent = replaceWinAndLosePlaceholders(phraseContainer.getContent(), this.userProgress.getWins(), this.userProgress.getLosses());
                newContent = replaceAwardsPlaceholders(newContent, this.userProgress.getAchievements());
                if (!newContent.isEmpty()) {
                    newDialog.add(new BasePhraseContainer(newContent, phraseContainer.getRole()));
                }
            }
        }
        builder.addResponse(getDialogTranslator().translate(newDialog, true));
    }

    private String replaceWinAndLosePlaceholders(String inputString, Integer win, Integer lose) {
//        if ((win == 0 && lose == 0) && (inputString.contains("%wins%") || inputString.contains("%losses%"))) {
//            return "";
//        }
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
