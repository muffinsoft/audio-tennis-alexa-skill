package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.REPEAT_LAST_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.INTENT;

public class ResetConfirmationStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(CancelStateManager.class);

    private final RegularPhraseManager regularPhraseManager;

    public ResetConfirmationStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer.getDialogTranslator());
        this.regularPhraseManager = configContainer.getRegularPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {

        logger.debug("Session attributes on the start of handling: " + this.getSessionAttributes().toString());
    }

    @Override
    public DialogItem nextResponse() {
        logger.debug("Available session attributes: " + getSessionAttributes());

        logger.debug("User reply: " + getUserReply());

        String dialog = null;

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            getSessionAttributes().put(INTENT, IntentType.GAME);
        }
        else if (UserReplyComparator.compare(getUserReply(), UserReplies.YES)) {
            getSessionAttributes().put(INTENT, IntentType.GAME);
        }
        else {
            dialog = regularPhraseManager.getValueByKey(REPEAT_LAST_PHRASE);
        }

        DialogItem.Builder builder = DialogItem.builder().addResponse(getDialogTranslator().translate(dialog));

        return builder.build();
    }
}