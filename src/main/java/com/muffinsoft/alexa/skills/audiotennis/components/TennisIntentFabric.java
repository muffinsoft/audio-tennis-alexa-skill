package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.skills.audiotennis.activities.CancelStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ExitStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.HelpStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetConfirmationStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.AlphabetRaceGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.LastLetterGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.OnomatopoeiaGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.RhymeMatchGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;

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

        ActivityProgress gameActivity = getCurrentGameActivity(attributesManager);
        switch (gameActivity.getCurrentActivity()) {
            case ALPHABET_RACE:
                return new AlphabetRaceGamePhaseStateManager(inputSlots, attributesManager, configContainer);
            case LAST_LETTER:
                return new LastLetterGamePhaseStateManager(inputSlots, attributesManager, configContainer);
            case RHYME_MATCH:
                return new RhymeMatchGamePhaseStateManager(inputSlots, attributesManager, configContainer);
            case ONOMATOPOEIA:
                return new OnomatopoeiaGamePhaseStateManager(inputSlots, attributesManager, configContainer);
            default:
                throw new IllegalArgumentException("Can't create instance of activity handler for type " + gameActivity.getCurrentActivity());
        }
    }

    private ActivityProgress getCurrentGameActivity(AttributesManager attributesManager) {

        LinkedHashMap rawActivityProgress = (LinkedHashMap) attributesManager.getSessionAttributes().get(ACTIVITY_PROGRESS);

        return rawActivityProgress != null ? new ObjectMapper().convertValue(rawActivityProgress, ActivityProgress.class) : ActivityProgress.createDefault();
    }
}
