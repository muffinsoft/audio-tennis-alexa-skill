package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.ResetIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_RESET_PROGRESS_PHRASE;

public class TennisResetIntentHandler extends ResetIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisResetIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.regularPhraseManager = configurationContainer.getRegularPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_RESET_PROGRESS_PHRASE);
    }
}
