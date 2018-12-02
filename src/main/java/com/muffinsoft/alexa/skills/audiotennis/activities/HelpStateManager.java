package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.ACTIVITY_ALPHABET_RACE_HELP_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.ACTIVITY_BAM_WHAM_HELP_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.ACTIVITY_LAST_LETTER_HELP_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.ACTIVITY_RHYME_MATCH_HELP_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.GENERAL_HELP_PHRASE;

public class HelpStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(HelpStateManager.class);
    private final RegularPhraseManager regularPhraseManager;
    private ActivityProgress activityProgress;

    public HelpStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
        this.activityProgress = rawActivityProgress != null ? mapper.convertValue(rawActivityProgress, ActivityProgress.class) : null;
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        List<PhraseContainer> generalHelp = regularPhraseManager.getValueByKey(GENERAL_HELP_PHRASE);

        builder.addResponse(getDialogTranslator().translate(generalHelp));

        if (this.activityProgress != null && this.activityProgress.getCurrentActivity() != null) {
            List<PhraseContainer> activityHelp = Collections.emptyList();
            switch (activityProgress.getCurrentActivity()) {
                case RHYME_MATCH:
                    activityHelp = regularPhraseManager.getValueByKey(ACTIVITY_RHYME_MATCH_HELP_PHRASE);
                    break;
                case LAST_LETTER:
                    activityHelp = regularPhraseManager.getValueByKey(ACTIVITY_LAST_LETTER_HELP_PHRASE);
                    break;
                case BAM_WHAM:
                    activityHelp = regularPhraseManager.getValueByKey(ACTIVITY_BAM_WHAM_HELP_PHRASE);
                    break;
                case ALPHABET_RACE:
                    activityHelp = regularPhraseManager.getValueByKey(ACTIVITY_ALPHABET_RACE_HELP_PHRASE);
                    break;
            }
            builder.addResponse(getDialogTranslator().translate(activityHelp));
        }

        getSessionAttributes().put(SessionConstants.INTENT, IntentType.GAME);

        return builder.build();
    }
}
