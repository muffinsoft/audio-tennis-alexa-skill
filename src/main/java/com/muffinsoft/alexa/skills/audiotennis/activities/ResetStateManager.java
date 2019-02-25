package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.sdk.enums.IntentType.GAME;
import static com.muffinsoft.alexa.sdk.model.SlotName.CONFIRMATION;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.REPEAT_LAST_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.RETURN_TO_GAME_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.INTENT;

public class ResetStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(CancelStateManager.class);

    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;

    public ResetStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
    }

    @Override
    protected void updatePersistentAttributes() {
        getPersistentAttributes().remove(USER_PROGRESS);
    }

    @Override
    public DialogItem nextResponse() {

        logger.debug("Available session attributes: " + getSessionAttributes());

        DialogItem.Builder builder = DialogItem.builder();

        if (UserReplyComparator.compare(getUserReply(CONFIRMATION), UserReplies.NO)) {
            getSessionAttributes().put(INTENT, IntentType.GAME);
            getSessionAttributes().put(STATE_TYPE, StateType.ACTIVITY_INTRO);
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(RETURN_TO_GAME_PHRASE), true));
        }
        else if (UserReplyComparator.compare(getUserReply(CONFIRMATION), UserReplies.YES)) {
            getSessionAttributes().remove(ACTIVITY_PROGRESS);
            getSessionAttributes().put(INTENT, IntentType.GAME);
            getSessionAttributes().put(STATE_TYPE, StateType.ACTIVITY_INTRO);
            addFirstActivityIntro(builder);
            savePersistentAttributes();
        }
        else {
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(REPEAT_LAST_PHRASE), true));
        }

        return builder.build();
    }

    private void addFirstActivityIntro(DialogItem.Builder builder) {

        List<BasePhraseContainer> dialog = activitiesPhraseManager.getPhrasesForActivity(ActivityProgress.getDefaultActivity()).getIntro();

        int index = 0;
        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                this.getSessionAttributes().put(INTENT, GAME);
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings, true));
        }

        if (index >= dialog.size()) {
            this.getSessionAttributes().put(STATE_TYPE, StateType.READY);
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(PhraseConstants.READY_TO_STATE_PHRASE), true));
        }
    }
}
