package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class HelpStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(HelpStateManager.class);

    private final PhraseManager phraseManager;

    public HelpStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer.getDialogTranslator());
        this.phraseManager = configContainer.getPhraseManager();
    }

    @Override
    public DialogItem nextResponse() {

        return DialogItem.builder().build();
    }
}
