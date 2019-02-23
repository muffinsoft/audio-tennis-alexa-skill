package com.muffinsoft.alexa.skills.audiotennis.components;

import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.enums.StateType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AplManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.STATE_TYPE;
import static com.muffinsoft.alexa.sdk.enums.IntentType.GAME;

public class ActivitySelectionAppender {

    private final DialogTranslator dialogTranslator;
    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final AplManager aplManager;
    private final CardManager cardManager;

    public ActivitySelectionAppender(PhraseDependencyContainer phraseDependencyContainer, AplManager aplManager, CardManager cardManager, DialogTranslator dialogTranslator) {
        this.dialogTranslator = dialogTranslator;
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.activitiesPhraseManager = phraseDependencyContainer.getActivitiesPhraseManager();
        this.aplManager = aplManager;
        this.cardManager = cardManager;
    }

    public boolean appendWithSelection(DialogItem.Builder builder, UserProgress userProgress, Map<String, Object> sessionAttributes) {

        List<PhraseContainer> dialog;

        switch (userProgress.getUnlockedActivities().size()) {
            case 0:
            case 1:
                builder.addResponse(dialogTranslator.translate(getFirstActivityIntro(sessionAttributes)));
                return false;
            case 2:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_TWO_PHRASE);
                break;
            case 3:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_THREE_PHRASE);
                break;
            default:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_ALL_PHRASE);
                break;
        }

        builder
                .withAplDocument(aplManager.getImageDocument())
                .addBackgroundImageUrl(cardManager.getValueByKey("select-activity"))
                .addResponse(dialogTranslator.translate(dialog));
        return true;
    }

    private List<PhraseContainer> getFirstActivityIntro(Map<String, Object> sessionAttributes) {

        List<BasePhraseContainer> dialog = activitiesPhraseManager.getPhrasesForActivity(ActivityProgress.getDefaultActivity()).getIntro();

        List<PhraseContainer> result = new ArrayList<>();

        int index = 0;
        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (phraseSettings.isUserResponse()) {
                sessionAttributes.put(SessionConstants.USER_REPLY_BREAKPOINT, index);
                sessionAttributes.put(INTENT, GAME);
                break;
            }
            result.add(phraseSettings);
        }

        if (index >= dialog.size()) {
            sessionAttributes.put(STATE_TYPE, StateType.READY);
            result.addAll(regularPhraseManager.getValueByKey(PhraseConstants.READY_TO_STATE_PHRASE));
        }
        return result;
    }
}
