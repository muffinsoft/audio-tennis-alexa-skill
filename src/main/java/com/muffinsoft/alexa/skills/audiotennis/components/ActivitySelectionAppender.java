package com.muffinsoft.alexa.skills.audiotennis.components;

import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.List;
import java.util.Map;

public class ActivitySelectionAppender {

    private final DialogTranslator dialogTranslator;
    private final RegularPhraseManager regularPhraseManager;

    public ActivitySelectionAppender(PhraseDependencyContainer phraseDependencyContainer, DialogTranslator dialogTranslator) {
        this.dialogTranslator = dialogTranslator;
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    public void append(DialogItem.Builder builder, UserProgress userProgress, Map<String, Object> sessionAttributes) {

        List<PhraseContainer> dialog;

        switch (userProgress.getUnlockedActivities().size()) {
            case 0:
            case 1:
                dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_ACTIVITY_BETWEEN_ONE_PHRASE);
                sessionAttributes.put(SessionConstants.EXIT_FROM_ONE_POSSIBLE_ACTIVITY, true);
                break;
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

        builder.addResponse(dialogTranslator.translate(dialog));
    }
}
