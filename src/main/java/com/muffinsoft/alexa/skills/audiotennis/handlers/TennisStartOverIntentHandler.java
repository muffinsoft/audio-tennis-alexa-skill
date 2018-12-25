package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.StartOverIntentHandler;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.List;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_RESET_PROGRESS_PHRASE;

public class TennisStartOverIntentHandler extends StartOverIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisStartOverIntentHandler(SettingsDependencyContainer configurationContainer, PhraseDependencyContainer phraseDependencyContainer) {
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected List<PhraseContainer> getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_RESET_PROGRESS_PHRASE);
    }
}
