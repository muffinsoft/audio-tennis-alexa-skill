package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseGameStateManager;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.UserProgressConverter;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GeneralActivityPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityPhrases;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivitySettings;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.EXIT_PHRASE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.sdk.enums.StateType.ACTIVITY_INTRO;
import static com.muffinsoft.alexa.sdk.enums.StateType.READY;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.UNKNOWN_WORD_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;

public abstract class TennisBaseGameStateManager extends BaseGameStateManager {

    protected final RegularPhraseManager regularPhraseManager;
    final ActivitiesPhraseManager activitiesPhraseManager;
    final AliasManager aliasManager;
    final ProgressManager progressManager;
    final ActivityManager activityManager;
    final GeneralActivityPhraseManager generalActivityPhraseManager;
    final String enemyRole;
    UserProgress userProgress;
    ActivityProgress activityProgress;
    ActivityType currentActivityType;
    ActivityPhrases phrasesForActivity;
    ActivitySettings settingsForActivity;
    StateType stateType;
    private Integer userReplyBreakpointPosition;

    TennisBaseGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.aliasManager = settingsDependencyContainer.getAliasManager();
        this.progressManager = settingsDependencyContainer.getProgressManager();
        this.activityManager = settingsDependencyContainer.getActivityManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
        this.generalActivityPhraseManager = phraseDependencyContainer.getGeneralActivityPhraseManager();
        this.enemyRole = progressManager.getEnemyRole();
    }

    @Override
    protected void populateActivityVariables() {
        this.stateType = StateType.valueOf(String.valueOf(getSessionAttributes().getOrDefault(STATE_TYPE, ACTIVITY_INTRO)));

        this.userReplyBreakpointPosition = (Integer) this.getSessionAttributes().getOrDefault(USER_REPLY_BREAKPOINT, null);

        UserProgress userProgress = UserProgressConverter.fromJson(String.valueOf(getPersistentAttributes().get(USER_PROGRESS)));
        this.userProgress = userProgress != null ? userProgress : new UserProgress(this.currentActivityType);

        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        ActivityProgress activityProgress = rawActivityProgress != null ? mapper.convertValue(rawActivityProgress, ActivityProgress.class) : new ActivityProgress(this.currentActivityType, true);
        activityProgress.getUnlockedActivities().remove(null);
        this.activityProgress = activityProgress;
    }

    @Override
    protected void updateSessionAttributes() {
        this.getSessionAttributes().put(STATE_TYPE, this.stateType);
        this.getSessionAttributes().put(ACTIVITY_PROGRESS, this.activityProgress);
    }

    @Override
    protected void updatePersistentAttributes() {

        this.userProgress.setLastGameEnemyPoint(this.activityProgress.getEnemyPointCounter());
        this.userProgress.setLastGamePlayerPoint(this.activityProgress.getPlayerPointCounter());

        if (!this.userProgress.getUnlockedActivities().contains(this.currentActivityType.name())) {
            this.userProgress.addUnlockedActivity(this.currentActivityType);
        }

        String json = UserProgressConverter.toJson(this.userProgress);
        if (json != null) {
            this.getPersistentAttributes().put(SessionConstants.USER_PROGRESS, json);
        }
    }

    @Override
    protected DialogItem.Builder populateResponse(DialogItem.Builder builder) {

        phrasesForActivity = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType);
        settingsForActivity = activityManager.getSettingsForActivity(this.currentActivityType);

        if (activityProgress.isNew()) {
            activityProgress.fromUserProgress(userProgress);
            activityProgress.updateWithDifficultSettings(settingsForActivity);
        }

        builder = handleStateAction(stateType, builder);

        return builder;
    }

    @Override
    protected boolean isIntercepted() {
        String userReply = getUserReply();
        return !activityManager.isKnownWord(userReply);
    }

    @Override
    protected DialogItem.Builder handleInterception(DialogItem.Builder builder) {

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(UNKNOWN_WORD_PHRASE)));

        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleReadyToPlayState(DialogItem.Builder builder) {

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            appendActivitySelection(builder);
        }
        else {
            initGameStatePhrase(builder);
        }
        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleReturnToGameState(DialogItem.Builder builder) {
        this.stateType = StateType.GAME_PHASE_1;

        String word;
        if (this.activityProgress.getPreviousWord() == null) {
            word = generateRandomWord();
        }
        else {
            word = this.activityProgress.getPreviousWord();
        }
        builder.addResponse(getDialogTranslator().translate(word));

        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleRestartState(DialogItem.Builder builder) {

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(EXIT_PHRASE)));
            builder.withShouldEnd(true);
        }
        else {
            initGameStatePhrase(builder);
        }
        return builder.withSlotName(actionSlotName);
    }

    @SuppressWarnings("Duplicates")
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

        getSessionAttributes().put(SWITCH_ACTIVITY_STEP, true);
        builder.addResponse(getDialogTranslator().translate(dialog));
    }

    @Override
    protected DialogItem.Builder handleActivityIntroState(DialogItem.Builder builder) {

        this.stateType = READY;

        List<BasePhraseContainer> dialog = activitiesPhraseManager.getPhrasesForActivity(currentActivityType).getIntro();

        int iterationPointer = wrapAnyUserResponse(dialog, builder);

        if (iterationPointer >= dialog.size()) {
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(PhraseConstants.READY_TO_STATE_PHRASE)));
        }

        return builder.withSlotName(actionSlotName);
    }

    private int wrapAnyUserResponse(List<BasePhraseContainer> dialog, DialogItem.Builder builder) {

        if (this.userReplyBreakpointPosition != null) {
            this.getSessionAttributes().remove(USER_REPLY_BREAKPOINT);
        }

        int index = 0;

        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (this.userReplyBreakpointPosition != null && index <= this.userReplyBreakpointPosition) {
                continue;
            }

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                this.stateType = StateType.ACTIVITY_INTRO;
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
        }
        return index;
    }

    String generateRandomWord() {

        WordContainer wordContainer = activityManager.getRandomWordForActivity(this.currentActivityType);

        String word = wordContainer.getWord();

        this.activityProgress.setPreviousWord(word);
        this.activityProgress.addUsedWord(word);

        if (wordContainer.getUserReaction() != null) {
            this.activityProgress.setRequiredUserReaction(wordContainer.getUserReaction());
        }

        return word;
    }

    void appendNextRoundPhrase(DialogItem.Builder builder) {
        BasePhraseContainer randomOpponentFirstPhrase = generalActivityPhraseManager.getGeneralActivityPhrases().getRandomNextRound();
        builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));
    }

    private void initGameStatePhrase(DialogItem.Builder builder) {
        this.stateType = StateType.GAME_PHASE_1;
        this.activityProgress.reset();

        BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.currentActivityType).getRandomOpponentFirstPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));

        builder.addResponse(getDialogTranslator().translate(generateRandomWord(), enemyRole));
    }
}
