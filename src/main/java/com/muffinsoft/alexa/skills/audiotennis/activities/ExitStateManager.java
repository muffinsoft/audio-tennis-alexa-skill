package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.EXIT_AFTER_CANCEL_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.REPEAT_LAST_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.RETURN_TO_GAME_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.INTENT;

public class ExitStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(CancelStateManager.class);

    private final RegularPhraseManager regularPhraseManager;

    public ExitStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    public DialogItem nextResponse() {

        logger.debug("Available session attributes: " + getSessionAttributes());

        DialogItem.Builder builder = DialogItem.builder();

        if (UserReplyComparator.compare(getUserReply(), UserReplies.NO)) {
            getSessionAttributes().put(INTENT, IntentType.GAME);
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(RETURN_TO_GAME_PHRASE)));
        }
        else {
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(EXIT_AFTER_CANCEL_PHRASE)));
            builder.shouldEnd();
        }

        return builder.withSlotName(actionSlotName).build();
    }
}
