package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.handlers.RefundIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;

public class TennisRefundIntentHandler extends RefundIntentHandler {

    private final DialogTranslator dialogTranslator;

    public TennisRefundIntentHandler(DialogTranslator dialogTranslator) {
        this.dialogTranslator = dialogTranslator;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {
        return new BaseStateManager(getSlotsFromInput(input), input.getAttributesManager(), dialogTranslator) {
            @Override
            public DialogItem nextResponse() {
                return null;
            }
        };
    }
}
