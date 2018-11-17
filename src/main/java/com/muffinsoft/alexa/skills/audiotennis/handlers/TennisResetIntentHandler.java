package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.ResetIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_RESET_PROGRESS_PHRASE;

public class TennisResetIntentHandler extends ResetIntentHandler {

    private final PhraseManager phraseManager;

    public TennisResetIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.phraseManager = configurationContainer.getPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return phraseManager.getValueByKey(WANT_RESET_PROGRESS_PHRASE);
    }
}
