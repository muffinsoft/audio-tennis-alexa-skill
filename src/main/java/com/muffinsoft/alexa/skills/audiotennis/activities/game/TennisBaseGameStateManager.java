package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseGameStateManager;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GeneralActivityPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;
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

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.sdk.enums.StateType.ACTIVITY_INTRO;
import static com.muffinsoft.alexa.sdk.enums.StateType.READY;

public abstract class TennisBaseGameStateManager extends BaseGameStateManager {

    protected final RegularPhraseManager regularPhraseManager;
    protected final ActivitiesPhraseManager activitiesPhraseManager;
    protected final AliasManager aliasManager;
    protected final ProgressManager progressManager;
    protected final ActivityManager activityManager;
    protected final UserReplyManager userReplyManager;
    protected final GeneralActivityPhraseManager generalActivityPhraseManager;
    StateType stateType;
    UserProgress userProgress;
    ActivityProgress activityProgress;
    ActivityType currentActivityType;
    ActivityPhrases phrasesForActivity;
    ActivitySettings settingsForActivity;
    private Integer userReplyBreakpointPosition;

    TennisBaseGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.aliasManager = settingsDependencyContainer.getAliasManager();
        this.progressManager = settingsDependencyContainer.getProgressManager();
        this.userReplyManager = settingsDependencyContainer.getUserReplyManager();
        this.activityManager = settingsDependencyContainer.getActivityManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
        this.generalActivityPhraseManager = phraseDependencyContainer.getGeneralActivityPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        this.stateType = StateType.valueOf(String.valueOf(getSessionAttributes().getOrDefault(STATE_TYPE, ACTIVITY_INTRO)));

        this.userReplyBreakpointPosition = (Integer) this.getSessionAttributes().getOrDefault(USER_REPLY_BREAKPOINT, null);

        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        this.activityProgress = rawActivityProgress != null ? mapper.convertValue(rawActivityProgress, ActivityProgress.class) : new ActivityProgress(this.currentActivityType);

        LinkedHashMap rawUserProgress = (LinkedHashMap) getSessionAttributes().get(USER_PROGRESS);
        this.userProgress = rawUserProgress != null ? mapper.convertValue(rawUserProgress, UserProgress.class) : new UserProgress();
    }

    @Override
    protected void updateSessionAttributes() {
        this.getSessionAttributes().put(STATE_TYPE, this.stateType);
        this.getSessionAttributes().put(ACTIVITY_PROGRESS, this.activityProgress);
    }

    @Override
    protected void updatePersistentAttributes() {
        super.updatePersistentAttributes();
    }

    @Override
    protected DialogItem.Builder populateResponse(DialogItem.Builder builder) {

        phrasesForActivity = activitiesPhraseManager.getPhrasesForActivity(this.currentActivityType);
        settingsForActivity = activityManager.getSettingsForActivity(this.currentActivityType);

        if (!activityProgress.isUpdateForLevel()) {
            activityProgress.updateWithDifficultSettings(settingsForActivity);
        }

        builder = handleStateAction(stateType, builder);

        // here I will modify response

        return builder;
    }

    @Override
    protected DialogItem.Builder handleReadyToPlayState(DialogItem.Builder builder) {

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            builder.addResponse(getDialogTranslator().translate("Here will be redirection to activity's selection"));
        }
        else {
            initGameStatePhrase(builder);
        }
        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleRestartState(DialogItem.Builder builder) {

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            builder.addResponse(getDialogTranslator().translate("Here will be exit"));
        }
        else {
            initGameStatePhrase(builder);
        }
        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleActivityIntroState(DialogItem.Builder builder) {

        this.stateType = READY;

        List<BasePhraseContainer> dialog = activitiesPhraseManager.getPhrasesForActivity(currentActivityType).getIntro();

        int iterationPointer = wrapAnyUserResponse(dialog, builder, StateType.ACTIVITY_INTRO);

        if (iterationPointer >= dialog.size()) {
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(PhraseConstants.READY_TO_STATE_PHRASE)));
        }

        return builder.withSlotName(actionSlotName);
    }

    private int wrapAnyUserResponse(List<BasePhraseContainer> dialog, DialogItem.Builder builder, StateType stateType) {

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
                this.stateType = stateType;
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
        }
        return index;
    }

    private String generateRandomWord() {

        WordContainer wordContainer = activityManager.getRandomWordForActivity(this.currentActivityType);

        String word = wordContainer.getWord();

        this.activityProgress.setPreviousWord(word);
        this.activityProgress.addUsedWord(word);

        if (wordContainer.getUserReaction() != null) {
            this.activityProgress.setRequiredUserReaction(wordContainer.getUserReaction());
        }

        return word;
    }

    private void initGameStatePhrase(DialogItem.Builder builder) {
        this.stateType = StateType.GAME_PHASE_1;
        this.activityProgress.reset();

        BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getPhrasesForActivity(this.currentActivityType).getRandomOpponentFirstPhrase();
        builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));

        String word = generateRandomWord();
        builder.addResponse(getDialogTranslator().translate(word));
    }
}
