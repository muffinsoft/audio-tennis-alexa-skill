package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.BaseDialogTranslator;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.constants.PaywallConstants;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.PurchaseState;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.sdk.util.SlotComputer;
import com.muffinsoft.alexa.skills.audiotennis.activities.*;
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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.*;
import static com.muffinsoft.alexa.sdk.enums.IntentType.*;
import static com.muffinsoft.alexa.sdk.enums.StateType.RETURN_TO_GAME;
import static com.muffinsoft.alexa.skills.audiotennis.components.ActivityPuller.getActivityFromReply;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.*;

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
            if (isPositiveReply(inputSlots)) {
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
            case BUY_INTENT:
                return buy(inputSlots, attributesManager);
            case UPSELL:
                return upsell(inputSlots, attributesManager, phraseDependencyContainer);
            default:
                throw new IllegalArgumentException("Can't create new Intent State object for type " + intent);
        }
    }

    private StateManager upsell(Map<String, Slot> slots, AttributesManager attributesManager, PhraseDependencyContainer phraseDependencyContainer) {
        return new BaseStateManager(slots, attributesManager, null) {
            @Override
            public DialogItem nextResponse() {
                return BuyManager.getBuyResponse(attributesManager, phraseDependencyContainer, new BaseDialogTranslator(), PaywallConstants.UPSELL);
            }
        };
    }

    private StateManager buy(Map<String, Slot> slots, AttributesManager attributesManager) {
        if (isPositiveReply(slots)) {
            return new BaseStateManager(slots, attributesManager, null) {
                @Override
                public DialogItem nextResponse() {
                    return DialogItem.builder().withDirective(PaywallConstants.BUY).build();
                }
            };
        } else {
            attributesManager.getSessionAttributes().put(STATE_TYPE, RETURN_TO_GAME);
            return getNextGameState(slots, attributesManager);
        }
    }

    private StateManager getNextGameState(Map<String, Slot> inputSlots, AttributesManager attributesManager) {

        ActivityProgress activityProgress = getCurrentActivityProgress(attributesManager);

        Map<String, Object> sessionAttributes = attributesManager.getSessionAttributes();
        String state = String.valueOf(attributesManager.getPersistentAttributes().getOrDefault(PURCHASE_STATE, PurchaseState.NOT_ENTITLED));
        PurchaseState purchaseState = PurchaseState.valueOf(state);

        IntentType interceptedIntentType = GAME;

        if (sessionAttributes.containsKey(SELECT_ACTIVITY_STEP)) {
            logger.info("SELECT_ACTIVITY_STEP is present");
            interceptedIntentType = interceptSelectActivity(inputSlots, sessionAttributes, activityProgress, purchaseState);
            logger.info("Intercepted IntentType:" + interceptedIntentType);
        }
        else if (sessionAttributes.containsKey(SWITCH_ACTIVITY_STEP)) {
            logger.info("SWITCH_ACTIVITY_STEP is present");
            interceptedIntentType = interceptActivityProgress(inputSlots, sessionAttributes, activityProgress, purchaseState);
            logger.info("Intercepted IntentType:" + interceptedIntentType);
        }
        else if (sessionAttributes.containsKey(SWITCH_UNLOCK_ACTIVITY_STEP)) {
            logger.info("SWITCH_UNLOCK_ACTIVITY_STEP is present");
            interceptedIntentType = interceptUnlockedActivityProgress(inputSlots, sessionAttributes, activityProgress, purchaseState);
            logger.info("Intercepted IntentType:" + interceptedIntentType);
        }
        else if (sessionAttributes.containsKey(ASK_RANDOM_SWITCH_ACTIVITY_STEP)) {
            logger.info("ASK_RANDOM_SWITCH_ACTIVITY_STEP is present");
            interceptedIntentType = interceptAskRandomActivityProgress(inputSlots, sessionAttributes, activityProgress, purchaseState);
            logger.info("Intercepted IntentType:" + interceptedIntentType);
        }

        if (interceptedIntentType != GAME) {
            return getNextState(interceptedIntentType, inputSlots, attributesManager);
        }

        ActivityType currentActivity = activityProgress.getCurrentActivity();

        if (currentActivity == null) {
            currentActivity = ActivityProgress.getDefaultActivity();
            logger.info("Set default activity: " + currentActivity);
        }
        else {
            logger.info("Open activity: " + currentActivity);
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

    private IntentType interceptAskRandomActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress, PurchaseState state) {

        ActivityType type = getActivityFromReply(inputSlots);

        if (type == ActivityType.ALPHABET_RACE || type == ActivityType.RHYME_MATCH) {
            if (state != PurchaseState.ENTITLED && sessionAttributes.containsKey(type.name())) {
                return UPSELL;
            } else {
                sessionAttributes.put(type.name(), "true");
            }
        }

        if (isPositiveReply(inputSlots)) {
            interceptRandomActivityProgress(sessionAttributes, activityProgress);
        }
        else {
            movingBetweenActivities(sessionAttributes, activityProgress, type);
        }
        sessionAttributes.remove(ASK_RANDOM_SWITCH_ACTIVITY_STEP);
        return GAME;
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
                sessionAttributes.put(STATE_TYPE, RETURN_TO_GAME);
            }
            else {
                sessionAttributes.put(STATE_TYPE, StateType.ACTIVITY_INTRO);
            }
        }
    }

    private IntentType interceptUnlockedActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress, PurchaseState state) {
        ActivityType type = getActivityFromReply(inputSlots);

        if (type == ActivityType.ALPHABET_RACE || type == ActivityType.RHYME_MATCH) {
            if (state != PurchaseState.ENTITLED && sessionAttributes.containsKey(type.name())) {
                return UPSELL;
            } else {
                sessionAttributes.put(type.name(), "true");
            }
        }

        if (isPositiveReply(inputSlots) && activityProgress.getPossibleActivity() != null) {
            moveToPossibleActivity(sessionAttributes, activityProgress);
        }
        else {
            movingBetweenActivities(sessionAttributes, activityProgress, type);
        }
        sessionAttributes.remove(SWITCH_UNLOCK_ACTIVITY_STEP);
        return GAME;
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

    private IntentType interceptSelectActivity(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress, PurchaseState state) {
        ActivityType type = getActivityFromReply(inputSlots);
        if (isSomethingElseReply(inputSlots)) {
            return SELECT_OTHER_MISSION;
        }
        else if (type == null) {
            return SELECT_MISSION;
        }
        else if (type == ActivityType.ALPHABET_RACE || type == ActivityType.RHYME_MATCH) {
            if (state != PurchaseState.ENTITLED && sessionAttributes.containsKey(type.name())) {
                return UPSELL;
            } else {
                sessionAttributes.put(type.name(), "true");
            }
        }
        logger.info("Update current activity type to value: " + type);
        activityProgress.setCurrentActivity(type);
        sessionAttributes.remove(SELECT_ACTIVITY_STEP);
        return GAME;
    }

    private IntentType interceptActivityProgress(Map<String, Slot> inputSlots, Map<String, Object> sessionAttributes, ActivityProgress activityProgress, PurchaseState state) {
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
            if (type == ActivityType.ALPHABET_RACE || type == ActivityType.RHYME_MATCH) {
                if (state != PurchaseState.ENTITLED && sessionAttributes.containsKey(type.name())) {
                    return UPSELL;
                } else {
                    sessionAttributes.put(type.name(), "true");
                }
            }
            movingBetweenActivities(sessionAttributes, activityProgress, type);
            sessionAttributes.remove(SWITCH_ACTIVITY_STEP);
        }
        return GAME;
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
