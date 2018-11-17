package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.FallbackIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.FALLBACK_PHRASE;

public class TennisFallbackIntentHandler extends FallbackIntentHandler {

    private final PhraseManager phraseManager;

    public TennisFallbackIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.phraseManager = configurationContainer.getPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return phraseManager.getValueByKey(FALLBACK_PHRASE);
    }
}
