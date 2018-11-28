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
import com.muffinsoft.alexa.skills.audiotennis.activities.InitialGreetingStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetConfirmationStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.AlphabetRaceGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.LastLetterGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.OnomatopoeiaGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.RhymeMatchGamePhaseStateManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;

public class TennisIntentFabric implements IntentFactory {

    private static final Logger logger = LogManager.getLogger(TennisIntentFabric.class);

    private final SettingsDependencyContainer settingsDependencyContainer;
    private final PhraseDependencyContainer phraseDependencyContainer;

    public TennisIntentFabric(SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        this.settingsDependencyContainer = settingsDependencyContainer;
        this.phraseDependencyContainer = phraseDependencyContainer;
    }

    public StateManager getNextState(IntentType intent, Map<String, Slot> inputSlots, AttributesManager attributesManager) {
        switch (intent) {
            case INITIAL_GREETING:
                return new InitialGreetingStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case HELP:
                return new HelpStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case RESET:
                return new ResetStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case RESET_CONFIRMATION:
                return new ResetConfirmationStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case EXIT:
                return new ExitStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case CANCEL:
                return new CancelStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
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
                return new AlphabetRaceGamePhaseStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case LAST_LETTER:
                return new LastLetterGamePhaseStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case RHYME_MATCH:
                return new RhymeMatchGamePhaseStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case ONOMATOPOEIA:
                return new OnomatopoeiaGamePhaseStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            default:
                throw new IllegalArgumentException("Can't create instance of activity handler for type " + gameActivity.getCurrentActivity());
        }
    }

    private ActivityProgress getCurrentGameActivity(AttributesManager attributesManager) {
        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();
        ActivityProgress activityProgress;
        if (sessionAttributes.containsKey(ACTIVITY_PROGRESS)) {
            LinkedHashMap rawActivityProgress = (LinkedHashMap) sessionAttributes.get(ACTIVITY_PROGRESS);
            activityProgress = new ObjectMapper().convertValue(rawActivityProgress, ActivityProgress.class);
            logger.debug("Current Activity Progress state: " + activityProgress);
        }
        else {
            activityProgress = ActivityProgress.createDefault();
            logger.debug("Was create default Activity Progress: " + activityProgress);
        }

        return activityProgress;
    }
}
