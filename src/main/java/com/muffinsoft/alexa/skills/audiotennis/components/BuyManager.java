package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.services.monetization.InSkillProduct;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.PurchaseState;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.util.PurchaseManager;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.PURCHASE_STATE;

public class BuyManager {

    public static DialogItem getBuyResponse(AttributesManager attributesManager, PhraseDependencyContainer phraseDependencyContainer, DialogTranslator dialogTranslator, String action) {

        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
        if (persistentAttributes == null) {
            persistentAttributes = new HashMap<>();
        }
        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();
        if (sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
            attributesManager.setSessionAttributes(sessionAttributes);
        }

        Object purchaseState = persistentAttributes.get(PURCHASE_STATE);
        PurchaseState state = null;
        if (purchaseState != null) {
            state = PurchaseState.valueOf(purchaseState.toString());
        }

        Object lastPurchase = persistentAttributes.get(SessionConstants.LAST_PURCHASE_ATTEMPT_ON);
        String dateTimeStr = lastPurchase == null ? null : String.valueOf(lastPurchase);
        ZonedDateTime lastPurchaseOn = dateTimeStr != null ? Instant.parse(dateTimeStr).atZone(ZoneOffset.systemDefault()) : ZonedDateTime.now();

        String key;
        if (state == PurchaseState.PENDING) {
            key = "purchaseWait";
        } else if (state == PurchaseState.DECLINED && Duration.between(lastPurchaseOn, ZonedDateTime.now()).toHours() <= 36) {
            key = "purchaseDeclined";
        } else if (state == PurchaseState.NOT_ENTITLED) {
            return DialogItem.builder().withDirective(action)
                    .build();
        } else if (state == PurchaseState.ENTITLED) {
            key = "purchaseAlreadyDone";
        } else {
            key = "purchaseNothing";
            sessionAttributes.put(INTENT, IntentType.SELECT_MISSION);
        }

        List<PhraseContainer> response = phraseDependencyContainer.getRegularPhraseManager().getValueByKey(key);
        return DialogItem.builder()
                .addResponse(dialogTranslator.translate(response, true))
                .withReprompt(dialogTranslator.translate(response, true))
                .build();
    }

    public static void verifyEntitled(HandlerInput input) {
        InSkillProduct product = PurchaseManager.getInSkillProduct(input);
        Map<String, Object> persistent = input.getAttributesManager().getPersistentAttributes();
        if (persistent == null) {
            persistent = new HashMap<>();
        }
        String state = String.valueOf(persistent.getOrDefault(PURCHASE_STATE, PurchaseState.NOT_ENTITLED));
        PurchaseState purchaseState = PurchaseState.valueOf(state);
        if (PurchaseManager.isEntitled(product)) {
            persistent.put(PURCHASE_STATE, PurchaseState.ENTITLED);
        } else if (purchaseState == PurchaseState.ENTITLED && !PurchaseManager.isEntitled(product)) {
            persistent.put(PURCHASE_STATE, PurchaseState.NOT_ENTITLED);
        }
        input.getAttributesManager().setPersistentAttributes(persistent);
        input.getAttributesManager().savePersistentAttributes();
    }
}
