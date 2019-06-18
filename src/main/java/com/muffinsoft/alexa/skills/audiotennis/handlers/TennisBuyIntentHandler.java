package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.services.monetization.InSkillProduct;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.constants.PaywallConstants;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.PurchaseState;
import com.muffinsoft.alexa.sdk.handlers.BuyIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.util.PurchaseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.PURCHASE_STATE;

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
                InSkillProduct product = PurchaseManager.getInSkillProduct(input);
                Object purchaseState = getPersistentAttributes().get(PURCHASE_STATE);
                PurchaseState state = null;
                if (purchaseState != null) {
                    state = PurchaseState.valueOf(purchaseState.toString());
                }

                Object lastPurchase = getPersistentAttributes().get(com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.LAST_PURCHASE_ATTEMPT_ON);
                String dateTimeStr = lastPurchase == null ? null : String.valueOf(lastPurchase);
                ZonedDateTime lastPurchaseOn = dateTimeStr != null ? Instant.parse(dateTimeStr).atZone(ZoneOffset.systemDefault()) : ZonedDateTime.now();

                String key;
                if (state == PurchaseState.PENDING) {
                    key = "purchaseWait";
                } else if (state == PurchaseState.DECLINED && Duration.between(lastPurchaseOn, ZonedDateTime.now()).toHours() <= 36) {
                    key = "purchaseDeclined";
                } else if (PurchaseManager.isAvailable(product)) {
                    return DialogItem.builder().withDirective(PaywallConstants.BUY)
                            .build();
                } else if (PurchaseManager.isEntitled(product)) {
                    key = "purchaseAlreadyDone";
                } else {
                    key = "purchaseNothing";
                    getSessionAttributes().put(INTENT, IntentType.SELECT_MISSION);
                }

                List<PhraseContainer> response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey(key);
                return DialogItem.builder()
                        .addResponse(dialogTranslator.translate(response, true))
                        .withReprompt(dialogTranslator.translate(response, true))
                        .build();
            }
        };
    }
}
