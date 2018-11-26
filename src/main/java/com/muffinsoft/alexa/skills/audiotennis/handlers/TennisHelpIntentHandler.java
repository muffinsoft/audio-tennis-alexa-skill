package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.HelpIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WANT_HELP_PHRASE;

public class TennisHelpIntentHandler extends HelpIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisHelpIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.regularPhraseManager = configurationContainer.getRegularPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_HELP_PHRASE);
    }
}