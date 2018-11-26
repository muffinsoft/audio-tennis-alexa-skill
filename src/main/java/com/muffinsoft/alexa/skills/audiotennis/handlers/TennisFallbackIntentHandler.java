package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.FallbackIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.FALLBACK_PHRASE;

public class TennisFallbackIntentHandler extends FallbackIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisFallbackIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.regularPhraseManager = configurationContainer.getRegularPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return regularPhraseManager.getValueByKey(FALLBACK_PHRASE);
    }
}
