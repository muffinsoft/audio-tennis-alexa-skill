package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.model.SlotName.CONFIRMATION;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.EXIT_AFTER_CANCEL_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.RETURN_TO_GAME_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.INTENT;

public class ExitStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(CancelStateManager.class);

    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;

    private ActivityProgress activityProgress;

    public ExitStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        ActivityProgress activityProgressFromRequest = mapper.convertValue(rawActivityProgress, ActivityProgress.class);
        if (activityProgressFromRequest != null) {
            activityProgressFromRequest.getUnlockedActivities().remove(null);
            this.activityProgress = activityProgressFromRequest;
        }
        else {
            this.activityProgress = null;
        }
    }

    @Override
    public DialogItem nextResponse() {

        logger.debug("Available session attributes: " + getSessionAttributes());

        DialogItem.Builder builder = DialogItem.builder();

        if (UserReplyComparator.compare(getUserReply(CONFIRMATION), UserReplies.NO)) {

            if (getSessionAttributes().containsKey(STATE_TYPE) && activityProgress != null) {
                if (StateType.valueOf(String.valueOf(getSessionAttributes().get(STATE_TYPE))) == StateType.GAME_PHASE_1) {
                    String previousWord = activityProgress.getPreviousWord();
                    if (previousWord != null && activityProgress.getCurrentActivity() != null) {
                        getSessionAttributes().put(INTENT, IntentType.GAME);
                        BasePhraseContainer randomOpponentFirstPhrase = activitiesPhraseManager.getGeneralPhrasesForActivity(this.activityProgress.getCurrentActivity()).getRandomOpponentFirstPhrase();
                        builder.addResponse(getDialogTranslator().translate(randomOpponentFirstPhrase));
                        builder.addResponse(getDialogTranslator().translate(previousWord));
                        return builder.build();
                    }
                }
            }

            getSessionAttributes().put(INTENT, IntentType.GAME);
            getSessionAttributes().put(STATE_TYPE, StateType.RETURN_TO_GAME);
            builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(RETURN_TO_GAME_PHRASE)));

            return builder.build();
        }

        builder.addResponse(getDialogTranslator().translate(regularPhraseManager.getValueByKey(EXIT_AFTER_CANCEL_PHRASE)));
        builder.shouldEnd();

        return builder.build();
    }
}
