package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.CancelIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_START_MISSION_PHRASE;

public class TennisCancelIntentHandler extends CancelIntentHandler {

    private final PhraseManager phraseManager;

    public TennisCancelIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.phraseManager = configurationContainer.getPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return phraseManager.getValueByKey(WANT_START_MISSION_PHRASE);
    }
}
