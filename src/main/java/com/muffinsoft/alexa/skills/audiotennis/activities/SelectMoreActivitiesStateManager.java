package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SELECT_ACTIVITY_STEP;

public class SelectMoreActivitiesStateManager extends BaseStateManager {

    private final RegularPhraseManager regularPhraseManager;

    public SelectMoreActivitiesStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        List<PhraseContainer> dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_OTHERS);
        getSessionAttributes().put(SELECT_ACTIVITY_STEP, true);
        builder.addResponse(getDialogTranslator().translate(dialog, true));

        return builder.build();
    }
}
