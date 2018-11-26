package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.REPEAT_LAST_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.RETURN_TO_GAME_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WANT_MISSION_FROM_BEGINNING_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.INTENT;

public class ResetStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(CancelStateManager.class);

    private final PhraseManager phraseManager;

    public ResetStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer.getDialogTranslator());
        this.phraseManager = configContainer.getPhraseManager();
    }

    @Override
    public DialogItem nextResponse() {

        logger.debug("Available session attributes: " + getSessionAttributes());

        DialogItem.Builder builder = DialogItem.builder();

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            getSessionAttributes().put(INTENT, IntentType.GAME);
            getSessionAttributes().put(STATE_TYPE, StateType.ACTIVITY_INTRO);
            builder.addResponse(getDialogTranslator().translate(phraseManager.getValueByKey(RETURN_TO_GAME_PHRASE)));
        }
        else if (UserReplyComparator.compare(getUserReply(), UserReplies.YES)) {
            builder.addResponse(getDialogTranslator().translate(phraseManager.getValueByKey(WANT_MISSION_FROM_BEGINNING_PHRASE)));
            getSessionAttributes().put(INTENT, IntentType.RESET_CONFIRMATION);
        }
        else {
            builder.addResponse(getDialogTranslator().translate(phraseManager.getValueByKey(REPEAT_LAST_PHRASE)));
        }

        return builder.build();
    }
}
