package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.services.monetization.InSkillProduct;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.PurchaseState;
import com.muffinsoft.alexa.sdk.handlers.PurchaseHistoryHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.util.PurchaseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;

public class TennisPurchaseHistoryHandler extends PurchaseHistoryHandler {

    private final DialogTranslator dialogTranslator;
    private final PhraseDependencyContainer phraseDependencyContainer;

    public TennisPurchaseHistoryHandler(DialogTranslator dialogTranslator, PhraseDependencyContainer phraseDependencyContainer) {
        this.dialogTranslator = dialogTranslator;
        this.phraseDependencyContainer = phraseDependencyContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {
        return new BaseStateManager(getSlotsFromInput(input), input.getAttributesManager(), dialogTranslator) {
            @Override
            public DialogItem nextResponse() {
                InSkillProduct product = PurchaseManager.getInSkillProduct(input);
                PurchaseState previousState = getPreviousPurchaseState(input);
                List<PhraseContainer> response;
                boolean arePurchasesEnabled = (boolean) getSessionAttributes().get("arePurchasesEnabled");
                if(PurchaseManager.isEntitled(product)) {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchaseHistory");
                } else if (!arePurchasesEnabled) {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("unrecognized");
                } else if (PurchaseManager.isPending(product, previousState)) {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchasePending");
                } else if (PurchaseManager.isAvailable(product)) {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchaseHistoryNothing");
                } else {
                    response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey("purchaseNothing");
                }
                getSessionAttributes().put(INTENT, IntentType.GAME);
                return DialogItem.builder()
                        .addResponse(dialogTranslator.translate(response, true))
                        .withReprompt(dialogTranslator.translate(response, true))
                        .build();
            }
        };
    }

    public static PurchaseState getPreviousPurchaseState(HandlerInput input) {
        Map<String, Object> persistent = input.getAttributesManager().getPersistentAttributes();
        if (persistent == null) {
            persistent = new HashMap<>();
        }
        PurchaseState previousState = null;
        Object previousStateObj = persistent.get("purchaseState");
        if (previousStateObj != null) {
            previousState = PurchaseState.valueOf(previousStateObj.toString());
        }
        return previousState;
    }
}
