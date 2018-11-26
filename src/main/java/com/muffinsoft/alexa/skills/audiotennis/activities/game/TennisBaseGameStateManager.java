package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseGameStateManager;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.sdk.enums.StateType.ACTIVITY_INTRO;
import static com.muffinsoft.alexa.sdk.enums.StateType.DEMO;
import static com.muffinsoft.alexa.sdk.enums.StateType.READY;

public abstract class TennisBaseGameStateManager extends BaseGameStateManager {

    protected final PhraseManager phraseManager;
    protected final AliasManager aliasManager;
    protected final UserReplyManager userReplyManager;

    StateType stateType;
    UserProgress userProgress;
    ActivityProgress activityProgress;

    TennisBaseGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer.getDialogTranslator());
        this.phraseManager = configContainer.getPhraseManager();
        this.aliasManager = configContainer.getAliasManager();
        this.userReplyManager = configContainer.getUserReplyManager();
    }

    @Override
    protected void populateActivityVariables() {
        stateType = StateType.valueOf(String.valueOf(getSessionAttributes().getOrDefault(STATE_TYPE, ACTIVITY_INTRO)));

        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        this.activityProgress = rawActivityProgress != null ? mapper.convertValue(rawActivityProgress, ActivityProgress.class) : new ActivityProgress();

        LinkedHashMap rawUserProgress = (LinkedHashMap) getSessionAttributes().get(USER_PROGRESS);
        this.userProgress = rawUserProgress != null ? mapper.convertValue(rawUserProgress, UserProgress.class) : new UserProgress();
    }

    @Override
    protected void updateSessionAttributes() {
        super.updateSessionAttributes();
    }

    @Override
    protected void updatePersistentAttributes() {
        super.updatePersistentAttributes();
    }

    @Override
    protected DialogItem.Builder populateResponse(DialogItem.Builder builder) {

        builder = handleStateAction(stateType, builder);

        // here I will modify response

        return builder;
    }

    @Override
    protected DialogItem.Builder handleWinState(DialogItem.Builder builder) {
        return super.handleWinState(builder);
    }

    @Override
    protected DialogItem.Builder handleLoseState(DialogItem.Builder builder) {
        return super.handleLoseState(builder);
    }

    @Override
    protected DialogItem.Builder handleReadyToPlayState(DialogItem.Builder builder) {
        this.stateType = StateType.GAME_PHASE_1;
        return super.handleReadyToPlayState(builder);
    }

    @Override
    protected DialogItem.Builder handleDemoState(DialogItem.Builder builder) {
        this.stateType = READY;
        return builder.withSlotName(actionSlotName);
    }

    @Override
    protected DialogItem.Builder handleActivityIntroState(DialogItem.Builder builder) {

        // should run demo
        if (true) {
            this.stateType = DEMO;
        }
        else {
            this.stateType = READY;
        }

        return builder.withSlotName(actionSlotName);
    }
}
