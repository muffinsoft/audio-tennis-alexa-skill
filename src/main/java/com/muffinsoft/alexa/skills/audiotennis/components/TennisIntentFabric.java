package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.skills.audiotennis.activities.CancelStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ExitStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.HelpStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetConfirmationStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.TennisGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.Map;

public class TennisIntentFabric implements IntentFactory {

    private final ConfigContainer configContainer;

    public TennisIntentFabric(ConfigContainer configContainer) {
        this.configContainer = configContainer;
    }

    public StateManager getNextState(IntentType intent, Map<String, Slot> inputSlots, AttributesManager attributesManager) {
        switch (intent) {
            case HELP:
                return new HelpStateManager(inputSlots, attributesManager, configContainer);
            case RESET:
                return new ResetStateManager(inputSlots, attributesManager, configContainer);
            case RESET_CONFIRMATION:
                return new ResetConfirmationStateManager(inputSlots, attributesManager, configContainer);
            case EXIT:
                return new ExitStateManager(inputSlots, attributesManager, configContainer);
            case CANCEL:
                return new CancelStateManager(inputSlots, attributesManager, configContainer);
            case GAME:
                return getNextGameState(inputSlots, attributesManager);
            default:
                throw new IllegalArgumentException("Can't create new Intent State object for type " + intent);
        }
    }

    private StateManager getNextGameState(Map<String, Slot> inputSlots, AttributesManager attributesManager) {
        return new TennisGamePhaseStateManager(inputSlots, attributesManager, configContainer);
    }
}
