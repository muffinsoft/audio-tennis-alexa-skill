package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.HelpIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WANT_HELP_PHRASE;

public class TennisHelpIntentHandler extends HelpIntentHandler {

    private final PhraseManager phraseManager;

    public TennisHelpIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.phraseManager = configurationContainer.getPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return phraseManager.getValueByKey(WANT_HELP_PHRASE);
    }
}