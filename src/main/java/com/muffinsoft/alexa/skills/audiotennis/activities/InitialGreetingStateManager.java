package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.sdk.enums.IntentType.GAME;
import static com.muffinsoft.alexa.sdk.enums.IntentType.INITIAL_GREETING;

public class InitialGreetingStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(InitialGreetingStateManager.class);

    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final RegularPhraseManager regularPhraseManager;

    private Integer userReplyBreakpointPosition;
    private ActivityProgress activityProgress;

    public InitialGreetingStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        this.userReplyBreakpointPosition = (Integer) this.getSessionAttributes().getOrDefault(USER_REPLY_BREAKPOINT, null);

        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        this.activityProgress = rawActivityProgress != null ? mapper.convertValue(rawActivityProgress, ActivityProgress.class) : ActivityProgress.createDefault();
    }

    @Override
    protected void updateSessionAttributes() {
        this.getSessionAttributes().put(ACTIVITY_PROGRESS, this.activityProgress);
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        List<BasePhraseContainer> dialog = activitiesPhraseManager.getGreetingsPhrases().getFirstTimeGreeting();

        this.getSessionAttributes().remove(USER_REPLY_BREAKPOINT);
        this.getSessionAttributes().put(INTENT, GAME);

        if (this.userReplyBreakpointPosition != null) {
            this.getSessionAttributes().remove(USER_REPLY_BREAKPOINT);
        }

        int index = 0;

        for (BasePhraseContainer phraseSettings : dialog) {

            index++;

            if (this.userReplyBreakpointPosition != null && index <= this.userReplyBreakpointPosition) {
                continue;
            }

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                this.getSessionAttributes().put(INTENT, INITIAL_GREETING);
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
        }

        if (index >= dialog.size()) {
            this.userReplyBreakpointPosition = null;
            addFirstActivityIntro(builder);
        }

        return builder.withSlotName(actionSlotName).turnOffReprompt().build();
    }

    private void addFirstActivityIntro(DialogItem.Builder builder) {

        List<BasePhraseContainer> dialog = activitiesPhraseManager.getPhrasesForActivity(this.activityProgress.getCurrentActivity()).getIntro();

        int index = 0;
        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (this.userReplyBreakpointPosition != null && index <= this.userReplyBreakpointPosition) {
                continue;
            }

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                this.getSessionAttributes().put(INTENT, GAME);
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
        }

        if (index >= dialog.size()) {
            this.getSessionAttributes().put(STATE_TYPE, StateType.READY);
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(PhraseConstants.READY_TO_STATE_PHRASE)));
        }
    }
}
