package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.StopIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.EXIT_PHRASE;

public class TennisStopIntentHandler extends StopIntentHandler {

    private final PhraseManager phraseManager;

    public TennisStopIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.phraseManager = configurationContainer.getPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return phraseManager.getValueByKey(EXIT_PHRASE);
    }
}
