package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.sdk.util.SlotComputer;
import com.muffinsoft.alexa.skills.audiotennis.activities.CancelStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ExitStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.FallbackStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.HelpStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.InitialGreetingStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetConfirmationStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.AlphabetRaceGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.BamWhamGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.LastLetterGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.game.RhymeMatchGameStateManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.RANDOM_SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;

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
            case FALLBACK:
                return new FallbackStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            default:
                throw new IllegalArgumentException("Can't create new Intent State object for type " + intent);
        }
    }

    private StateManager getNextGameState(Map<String, Slot> inputSlots, AttributesManager attributesManager) {

        ActivityProgress activityProgress = getCurrentActivityProgress(attributesManager);

        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();

        if (sessionAttributes.containsKey(SWITCH_ACTIVITY_STEP)) {
            interceptActivityProgress(inputSlots, sessionAttributes, activityProgress);
        }
        else if (sessionAttributes.containsKey(RANDOM_SWITCH_ACTIVITY_STEP)) {
            interceptRandomActivityProgress(sessionAttributes, activityProgress);
        }

        ActivityType currentActivity = activityProgress.getCurrentActivity();

        switch (currentActivity) {
            case ALPHABET_RACE:
                return new AlphabetRaceGameStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case LAST_LETTER:
                return new LastLetterGameStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case RHYME_MATCH:
                return new RhymeMatchGameStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case BAM_WHAM:
                return new BamWhamGameStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            default:
                throw new IllegalArgumentException("Can't create instance of activity handler for type " + currentActivity);
        }
    }

    private void interceptRandomActivityProgress(Map<String, Object> sessionAttributes, ActivityProgress activityProgress) {
        ActivityType currentActivity = activityProgress.getCurrentActivity();
        Set<ActivityType> unlockedActivities = new HashSet<>(activityProgress.getUnlockedActivities());
        unlockedActivities.remove(currentActivity);
        if (unlockedActivities.isEmpty()) {
            return;
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        ActivityType newActivity = unlockedActivities.stream().skip(random.nextInt(unlockedActivities.size())).findFirst().orElse(null);
        activityProgress.setPreviousActivity(currentActivity);
        activityProgress.setCurrentActivity(newActivity);
        sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
        sessionAttributes.remove(STATE_TYPE);
        sessionAttributes.remove(RANDOM_SWITCH_ACTIVITY_STEP);
    }

    private void interceptActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress) {
        ActivityType type = getActivityFromReply(inputSlots);

        if (isPositiveReply(inputSlots)) {
            ActivityType possibleActivity = activityProgress.getPossibleActivity();
            ActivityType currentActivity = activityProgress.getCurrentActivity();
            activityProgress.setCurrentActivity(possibleActivity);
            activityProgress.setPreviousActivity(currentActivity);
            sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
            sessionAttributes.remove(STATE_TYPE);
        }
        else if (type != null) {
            ActivityType currentActivity = activityProgress.getCurrentActivity();
            activityProgress.setCurrentActivity(type);
            activityProgress.setPreviousActivity(currentActivity);
            sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
            sessionAttributes.remove(STATE_TYPE);
        }
        sessionAttributes.remove(SWITCH_ACTIVITY_STEP);
    }

    private ActivityType getActivityFromReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots, SlotName.ACTION.text);
        for (String reply : userReplies) {
            if (UserReplyComparator.compare(reply, UserReplies.LAST_LETTER)) {
                return ActivityType.LAST_LETTER;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.BAM_WHAM)) {
                return ActivityType.BAM_WHAM;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.ALPHABET_RACE)) {
                return ActivityType.ALPHABET_RACE;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.RHYME_MATCH)) {
                return ActivityType.RHYME_MATCH;
            }
        }
        return null;
    }

    private boolean isPositiveReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots, SlotName.ACTION.text);
        for (String reply : userReplies) {
            if (UserReplyComparator.compare(reply, UserReplies.YES)) {
                return true;
            }
        }
        return false;
    }

    private ActivityProgress getCurrentActivityProgress(AttributesManager attributesManager) {
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
