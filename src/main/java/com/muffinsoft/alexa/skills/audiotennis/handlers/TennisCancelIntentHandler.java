package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.muffinsoft.alexa.sdk.handlers.CancelIntentHandler;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.List;

import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.EXIT_PHRASE;

public class TennisCancelIntentHandler extends CancelIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisCancelIntentHandler(SettingsDependencyContainer configurationContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected boolean shouldEnd() {
        return true;
    }

    @Override
    protected List<PhraseContainer> getPhrase() {
        return regularPhraseManager.getValueByKey(EXIT_PHRASE);
    }
}
