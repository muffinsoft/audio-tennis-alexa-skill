package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.handlers.FallbackIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.Speech;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.FALLBACK_PHRASE;

public class TennisFallbackIntentHandler extends FallbackIntentHandler {

    private final PhraseManager phraseManager;

    public TennisFallbackIntentHandler(ConfigContainer configurationContainer) {
        super();
        this.phraseManager = configurationContainer.getPhraseManager();
    }

    @Override
    public StateManager nextTurn(HandlerInput handlerInput) {
        return new BaseStateManager(getSlotsFromInput(handlerInput), handlerInput.getAttributesManager()) {
            @Override
            public DialogItem nextResponse() {
                return DialogItem.builder()
                        .withResponse(Speech.ofText(phraseManager.getValueByKey(FALLBACK_PHRASE)))
                        .build();
            }
        };
    }
}
