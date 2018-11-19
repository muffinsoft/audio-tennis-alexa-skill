package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.muffinsoft.alexa.sdk.model.Speech.ofAlexa;
import static com.muffinsoft.alexa.skills.audiotennis.constants.CardConstants.WELCOME_CARD;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WELCOME_BACK_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WELCOME_PHRASE;

public class LaunchStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(LaunchStateManager.class);
    private final PhraseManager phraseManager;
    private final CardManager cardManager;

    public LaunchStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager);
        this.phraseManager = configContainer.getPhraseManager();
        this.cardManager = configContainer.getCardManager();
    }

    private String buildRoyalGreeting() {
        return phraseManager.getValueByKey(WELCOME_BACK_PHRASE);
    }

    @Override
    public DialogItem nextResponse() {

        String speechText;

        if (false) {
            speechText = buildRoyalGreeting();

            logger.info("Existing user was started new Game Session. Start Royal Greeting");
        }
        else {
            speechText = phraseManager.getValueByKey(WELCOME_PHRASE);

            logger.info("New user was started new Game Session.");
        }

        return DialogItem.builder()
                .addResponse(ofAlexa(speechText))
                .withReprompt(ofAlexa(speechText))
                .withCardTitle(cardManager.getValueByKey(WELCOME_CARD))
                .build();
    }
}
