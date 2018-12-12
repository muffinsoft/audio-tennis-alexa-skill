package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;

public class FallbackStateManager extends BaseStateManager {

    private final RegularPhraseManager regularPhraseManager;

    public FallbackStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(PhraseConstants.UNKNOWN_WORD_PHRASE)));

        getSessionAttributes().put(SessionConstants.INTENT, IntentType.GAME);

        return builder.withSlotName(actionSlotName).build();
    }
}
