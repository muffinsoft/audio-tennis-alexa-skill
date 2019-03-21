package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.CustomResetIntentHandler;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.List;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_RESET_PROGRESS_PHRASE;

public class TennisResetIntentHandler extends CustomResetIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisResetIntentHandler(SettingsDependencyContainer configurationContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected List<PhraseContainer> getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_RESET_PROGRESS_PHRASE);
    }
}
