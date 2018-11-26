package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.StopIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.EXIT_PHRASE;

public class TennisStopIntentHandler extends StopIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisStopIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.regularPhraseManager = configurationContainer.getRegularPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return regularPhraseManager.getValueByKey(EXIT_PHRASE);
    }
}
