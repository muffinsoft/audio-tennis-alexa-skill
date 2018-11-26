package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.CancelIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_START_MISSION_PHRASE;

public class TennisCancelIntentHandler extends CancelIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisCancelIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.regularPhraseManager = configurationContainer.getRegularPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_START_MISSION_PHRASE);
    }
}
