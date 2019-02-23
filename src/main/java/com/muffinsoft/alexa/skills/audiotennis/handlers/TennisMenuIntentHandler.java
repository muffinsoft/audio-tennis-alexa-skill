package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.handlers.BaseRedirectionIntentHandler;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

import java.util.List;

import static com.amazon.ask.request.Predicates.intentName;
import static com.muffinsoft.alexa.sdk.constants.PhraseConstants.WANT_START_MISSION_PHRASE;

public class TennisMenuIntentHandler extends BaseRedirectionIntentHandler {

    private final RegularPhraseManager regularPhraseManager;

    public TennisMenuIntentHandler(SettingsDependencyContainer configurationContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected List<PhraseContainer> getPhrase() {
        return regularPhraseManager.getValueByKey(WANT_START_MISSION_PHRASE);
    }

    @Override
    protected IntentType getIntentType() {
        return IntentType.CANCEL;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MenuIntent"));
    }
}
