package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.ObjectConvert;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.sdk.model.SlotName.CONFIRMATION;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.REPEAT_LAST_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.WANT_EXIT_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.ASK_RANDOM_SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_ACTIVITY_STEP;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.SWITCH_UNLOCK_ACTIVITY_STEP;

public class CancelStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(CancelStateManager.class);

    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;

    private ActivityProgress activityProgress;

    public CancelStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        ActivityProgress activityProgressFromRequest = mapper.convertValue(rawActivityProgress, ActivityProgress.class);
        activityProgressFromRequest.getUnlockedActivities().remove(null);
        this.activityProgress = activityProgressFromRequest;
    }

    @Override
    public DialogItem nextResponse() {

        logger.debug("Available session attributes: " + getSessionAttributes());

        List<PhraseContainer> dialog;

        if (UserReplyComparator.compare(getUserReply(CONFIRMATION), UserReplies.YES)) {
            getSessionAttributes().remove(SWITCH_ACTIVITY_STEP);
            getSessionAttributes().remove(SWITCH_UNLOCK_ACTIVITY_STEP);
            getSessionAttributes().remove(ASK_RANDOM_SWITCH_ACTIVITY_STEP);

            dialog = updateSessionAttributesForRandomActivity(activityProgress);

            getSessionAttributes().put(INTENT, IntentType.GAME);
        }
        else if (UserReplyComparator.compare(getUserReply(CONFIRMATION), UserReplies.NO)) {
            dialog = regularPhraseManager.getValueByKey(WANT_EXIT_PHRASE);
            getSessionAttributes().put(INTENT, IntentType.EXIT);
        }
        else {
            dialog = regularPhraseManager.getValueByKey(REPEAT_LAST_PHRASE);
        }

        DialogItem.Builder builder = DialogItem.builder().addResponse(getDialogTranslator().translate(dialog, true));

        return builder.build();
    }

    private List<PhraseContainer> updateSessionAttributesForRandomActivity(ActivityProgress activityProgress) {

        ActivityType currentActivity = activityProgress.getCurrentActivity();

        Set<ActivityType> unlockedActivities = new HashSet<>(activityProgress.getUnlockedActivities());
        unlockedActivities.remove(currentActivity);
        if (unlockedActivities.isEmpty()) {
            unlockedActivities.add(currentActivity);
        }
        ActivityType newActivity = getRandomActivity(unlockedActivities);
        activityProgress.setTransition(true);
        activityProgress.setPreviousActivity(currentActivity);
        activityProgress.setCurrentActivity(newActivity);
        getSessionAttributes().put(ACTIVITY_PROGRESS, ObjectConvert.toMap(activityProgress));
        getSessionAttributes().put(STATE_TYPE, StateType.READY);
        getSessionAttributes().remove(USER_REPLY_BREAKPOINT);

        List<BasePhraseContainer> intro = activitiesPhraseManager.getPhrasesForActivity(newActivity).getIntro();
        List<PhraseContainer> phraseContainers = wrapIntroUserResponse(intro);

        if (phraseContainers.size() >= intro.size()) {
            phraseContainers.addAll(regularPhraseManager.getValueByKey(PhraseConstants.READY_TO_STATE_PHRASE));
        }
        return phraseContainers;
    }

    private List<PhraseContainer> wrapIntroUserResponse(List<BasePhraseContainer> dialog) {

        List<PhraseContainer> result = new ArrayList<>();
        int index = 0;

        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (phraseSettings.isUserResponse()) {
                getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                getSessionAttributes().put(STATE_TYPE, StateType.ACTIVITY_INTRO);
                break;
            }
            result.add(phraseSettings);
        }
        return result;
    }

    private ActivityType getRandomActivity(Set<ActivityType> unlockedActivities) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return unlockedActivities.stream().skip(random.nextInt(unlockedActivities.size())).findFirst().orElse(null);
    }
}
