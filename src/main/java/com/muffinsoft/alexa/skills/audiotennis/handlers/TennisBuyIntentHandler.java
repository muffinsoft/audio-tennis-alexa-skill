package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.constants.PaywallConstants;
import com.muffinsoft.alexa.sdk.handlers.BuyIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.BuyManager;
import com.muffinsoft.alexa.skills.audiotennis.components.Utils;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;

import java.util.List;

public class TennisBuyIntentHandler extends BuyIntentHandler {

    private final DialogTranslator dialogTranslator;
    private final PhraseDependencyContainer phraseDependencyContainer;

    public TennisBuyIntentHandler(DialogTranslator dialogTranslator, PhraseDependencyContainer phraseDependencyContainer) {
        this.dialogTranslator = dialogTranslator;
        this.phraseDependencyContainer = phraseDependencyContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {
        return new BaseStateManager(getSlotsFromInput(input), input.getAttributesManager(), dialogTranslator) {
            @Override
            public DialogItem nextResponse() {
                boolean isPurchasable = (boolean) input.getAttributesManager().getSessionAttributes().getOrDefault("isPurchasable", false);
                if (isPurchasable) {
                    Utils.saveProgressBeforeUpsell(this.getPersistentAttributes(), this.getSessionAttributes(), mapper);
                    savePersistentAttributes();
                    return BuyManager.getBuyResponse(input.getAttributesManager(), phraseDependencyContainer, dialogTranslator, PaywallConstants.BUY);
                } else {
                    List<PhraseContainer> response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchaseUnavailable");
                    return DialogItem.builder()
                            .addResponse(dialogTranslator.translate(response, true))
                            .withReprompt(dialogTranslator.translate(response, true))
                            .build();
                }
            }
        };
    }
}
