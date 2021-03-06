package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.services.monetization.EntitlementReason;
import com.amazon.ask.model.services.monetization.InSkillProduct;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.handlers.RefundIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.util.PurchaseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;

import java.util.List;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.CONTINUE_OR_MENU;

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
                boolean arePurchasesEnabled = (boolean) getSessionAttributes().get("arePurchasesEnabled");
                List<PhraseContainer> response;
                InSkillProduct product = PurchaseManager.getInSkillProduct(input);
                if (PurchaseManager.isEntitled(product) && product.getEntitlementReason() == EntitlementReason.AUTO_ENTITLED || !arePurchasesEnabled) {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("unrecognized");
                } else {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchaseNothingToRefund");
                    getSessionAttributes().put(CONTINUE_OR_MENU, "true");
                }
                getSessionAttributes().put(INTENT, IntentType.GAME);
                return DialogItem.builder()
                        .addResponse(dialogTranslator.translate(response, true))
                        .withReprompt(dialogTranslator.translate(response, true))
                        .build();
            }
        };
    }
}
