package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.sdk.util.SlotComputer;
import com.muffinsoft.alexa.skills.audiotennis.activities.CancelStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ExitStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ExitWithoutConfirmationStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.FallbackStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.HelpStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.InitialGreetingStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetConfirmationStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.ResetStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.SelectActivityStateManager;
import com.muffinsoft.alexa.skills.audiotennis.activities.SelectMoreActivitiesStateManager;
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
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.sdk.enums.IntentType.GAME;
import static com.muffinsoft.alexa.sdk.enums.IntentType.SELECT_MISSION;
import static com.muffinsoft.alexa.sdk.enums.IntentType.SELECT_OTHER_MISSION;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.ASK_RANDOM_SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.EXIT_FROM_HELP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.EXIT_FROM_ONE_POSSIBLE_ACTIVITY;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_UNLOCK_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.ALPHABET_RACE;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.BAM_WHAM;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.LAST_LETTER;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.RHYME_MATCH;

public class TennisIntentFabric implements IntentFactory {

    private static final Logger logger = LogManager.getLogger(TennisIntentFabric.class);

    private final SettingsDependencyContainer settingsDependencyContainer;
    private final PhraseDependencyContainer phraseDependencyContainer;

    public TennisIntentFabric(SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        this.settingsDependencyContainer = settingsDependencyContainer;
        this.phraseDependencyContainer = phraseDependencyContainer;
    }

    public StateManager getNextState(IntentType intent, Map<String, Slot> inputSlots, AttributesManager attributesManager) {

        if (attributesManager.getSessionAttributes().containsKey(EXIT_FROM_HELP)) {
            if (isNegativeReply(inputSlots)) {
                intent = IntentType.HELP;
            }
            attributesManager.getSessionAttributes().remove(EXIT_FROM_HELP);
        }

        if (attributesManager.getSessionAttributes().containsKey(EXIT_FROM_ONE_POSSIBLE_ACTIVITY)) {
            if (isNegativeReply(inputSlots)) {
                intent = IntentType.EXIT_CONFIRMATION;
            }
            attributesManager.getSessionAttributes().remove(EXIT_FROM_ONE_POSSIBLE_ACTIVITY);
        }

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
            case EXIT_CONFIRMATION:
                return new ExitWithoutConfirmationStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case CANCEL:
                return new CancelStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case FALLBACK:
                return new FallbackStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case SELECT_OTHER_MISSION:
                return new SelectMoreActivitiesStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case SELECT_MISSION:
                return new SelectActivityStateManager(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
            case GAME:
                return getNextGameState(inputSlots, attributesManager);
            default:
                throw new IllegalArgumentException("Can't create new Intent State object for type " + intent);
        }
    }

    private StateManager getNextGameState(Map<String, Slot> inputSlots, AttributesManager attributesManager) {

        ActivityProgress activityProgress = getCurrentActivityProgress(attributesManager);

        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();

        IntentType interceptedIntentType = GAME;

        if (sessionAttributes.containsKey(SWITCH_ACTIVITY_STEP)) {
            interceptedIntentType = interceptActivityProgress(inputSlots, sessionAttributes, activityProgress);
        }
        else if (sessionAttributes.containsKey(SWITCH_UNLOCK_ACTIVITY_STEP)) {
            interceptUnlockedActivityProgress(inputSlots, sessionAttributes, activityProgress);
        }
        else if (sessionAttributes.containsKey(ASK_RANDOM_SWITCH_ACTIVITY_STEP)) {
            interceptAskRandomActivityProgress(inputSlots, sessionAttributes, activityProgress);
        }

        if (interceptedIntentType != GAME) {
            return getNextState(interceptedIntentType, inputSlots, attributesManager);
        }

        ActivityType currentActivity = activityProgress.getCurrentActivity();

        if (currentActivity == null) {
            currentActivity = ActivityProgress.getDefaultActivity();
        }

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
        ActivityType newActivity = getRandomActivity(unlockedActivities);
        activityProgress.setTransition(true);
        activityProgress.setPreviousActivity(currentActivity);
        activityProgress.setCurrentActivity(newActivity);
        sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
        sessionAttributes.put(STATE_TYPE, StateType.ACTIVITY_INTRO);
        sessionAttributes.remove(USER_REPLY_BREAKPOINT);
        sessionAttributes.remove(STATE_TYPE);
    }

    private ActivityType getRandomActivity(Set<ActivityType> unlockedActivities) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return unlockedActivities.stream().skip(random.nextInt(unlockedActivities.size())).findFirst().orElse(null);
    }

    private void interceptAskRandomActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress) {

        ActivityType type = getActivityFromReply(inputSlots);

        if (isPositiveReply(inputSlots)) {
            interceptRandomActivityProgress(sessionAttributes, activityProgress);
        }
        else {
            movingBetweenActivities(sessionAttributes, activityProgress, type);
        }
        sessionAttributes.remove(ASK_RANDOM_SWITCH_ACTIVITY_STEP);
    }

    private void movingBetweenActivities(Map<String, Object> sessionAttributes, ActivityProgress activityProgress, ActivityType type) {
        if (type != null) {
            ActivityType currentActivity = activityProgress.getCurrentActivity();
            activityProgress.setCurrentActivity(type);
            activityProgress.setPreviousActivity(currentActivity);
            activityProgress.setTransition(true);
            sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
            sessionAttributes.remove(STATE_TYPE);
        }
        else {
            if (sessionAttributes.containsKey(STATE_TYPE)) {
                sessionAttributes.put(STATE_TYPE, StateType.RETURN_TO_GAME);
            }
            else {
                sessionAttributes.put(STATE_TYPE, StateType.ACTIVITY_INTRO);
            }
        }
    }

    private void interceptUnlockedActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress) {
        ActivityType type = getActivityFromReply(inputSlots);

        if (isPositiveReply(inputSlots) && activityProgress.getPossibleActivity() != null) {
            moveToPossibleActivity(sessionAttributes, activityProgress);
        }
        else {
            movingBetweenActivities(sessionAttributes, activityProgress, type);
        }
        sessionAttributes.remove(SWITCH_UNLOCK_ACTIVITY_STEP);
    }

    private void moveToPossibleActivity(Map<String, Object> sessionAttributes, ActivityProgress activityProgress) {
        ActivityType possibleActivity = activityProgress.getPossibleActivity();
        ActivityType currentActivity = activityProgress.getCurrentActivity();
        activityProgress.setTransition(true);
        activityProgress.setCurrentActivity(possibleActivity);
        activityProgress.setPreviousActivity(currentActivity);
        sessionAttributes.put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
        sessionAttributes.remove(STATE_TYPE);
        sessionAttributes.remove(SWITCH_ACTIVITY_STEP);
    }

    private IntentType interceptActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress) {
        ActivityType type = getActivityFromReply(inputSlots);

        if (isPositiveReply(inputSlots) && activityProgress.getPossibleActivity() != null) {
            moveToPossibleActivity(sessionAttributes, activityProgress);
        }
        else if (isNegativeReply(inputSlots)) {
            return SELECT_MISSION;
        }
        else if (isSomethingElseReply(inputSlots)) {
            return SELECT_OTHER_MISSION;
        }
        else {
            movingBetweenActivities(sessionAttributes, activityProgress, type);
            sessionAttributes.remove(SWITCH_ACTIVITY_STEP);
        }
        return GAME;
    }

    private ActivityType getActivityFromReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots).get(SlotName.MISSION);
        for (String reply : userReplies) {
            if (UserReplyComparator.compare(reply, UserReplies.LAST_LETTER)) {
                return LAST_LETTER;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.BAM_WHAM)) {
                return BAM_WHAM;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.ALPHABET_RACE)) {
                return ALPHABET_RACE;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.RHYME_MATCH)) {
                return RHYME_MATCH;
            }
        }
        return null;
    }

    private boolean isNegativeReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots).get(SlotName.CONFIRMATION);
        for (String reply : userReplies) {
            if (UserReplyComparator.compare(reply, UserReplies.NO)) {
                return true;
            }
        }
        return false;
    }


    private boolean isSomethingElseReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots).get(SlotName.MISSION);
        for (String reply : userReplies) {
            if (UserReplyComparator.compare(reply, UserReplies.SOMETHING_ELSE)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPositiveReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots).get(SlotName.CONFIRMATION);
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
        }
        else {
            activityProgress = ActivityProgress.createDefault();
        }

        return activityProgress;
    }
}
