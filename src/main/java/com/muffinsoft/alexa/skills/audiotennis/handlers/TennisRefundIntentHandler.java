package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.handlers.RefundIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;

import java.util.List;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;

public class TennisRefundIntentHandler extends RefundIntentHandler {

    private final DialogTranslator dialogTranslator;
    private final PhraseDependencyContainer phraseDependencyContainer;

    public TennisRefundIntentHandler(DialogTranslator dialogTranslator, PhraseDependencyContainer phraseDependencyContainer) {
        this.dialogTranslator = dialogTranslator;
        this.phraseDependencyContainer = phraseDependencyContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {
        return new BaseStateManager(getSlotsFromInput(input), input.getAttributesManager(), dialogTranslator) {
            @Override
            public DialogItem nextResponse() {
                List<PhraseContainer> response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchaseNothingToRefund");
                getSessionAttributes().put(INTENT, IntentType.GAME);
                return DialogItem.builder()
                        .addResponse(dialogTranslator.translate(response, true))
                        .withReprompt(dialogTranslator.translate(response, true))
                        .build();
            }
        };
    }
}
