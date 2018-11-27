package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.ResetIntentHandler;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_RESET_PROGRESS_PHRASE;

public class TennisResetIntentHandler extends ResetIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisResetIntentHandler(SettingsDependencyContainer configurationContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected String getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_RESET_PROGRESS_PHRASE);
    }
}
