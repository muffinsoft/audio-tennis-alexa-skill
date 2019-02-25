package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.EXIT_PHRASE;

public class ExitWithoutConfirmationStateManager extends BaseStateManager {

    private final RegularPhraseManager regularPhraseManager;

    public ExitWithoutConfirmationStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    public DialogItem nextResponse() {
        DialogItem.Builder builder = DialogItem.builder();

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(EXIT_PHRASE), true));
        builder.shouldEnd();

        return builder.build();
    }
}
