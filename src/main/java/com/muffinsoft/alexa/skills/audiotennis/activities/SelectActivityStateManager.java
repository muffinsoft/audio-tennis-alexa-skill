package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.ActivitySelectionAppender;
import com.muffinsoft.alexa.skills.audiotennis.components.UserProgressConverter;
import com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.BLOCKED_ACTIVITY_CALL;

public class SelectActivityStateManager extends BaseStateManager {

    private final ActivitySelectionAppender activitySelectionAppender;
    private final RegularPhraseManager regularPhraseManager;
    private final DialogTranslator dialogTranslator;

    private UserProgress userProgress;

    public SelectActivityStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
        this.activitySelectionAppender = settingsDependencyContainer.getActivitySelectionAppender();
        this.dialogTranslator = settingsDependencyContainer.getDialogTranslator();
    }

    @Override
    protected void populateActivityVariables() {
        UserProgress userProgress = UserProgressConverter.fromJson(String.valueOf(getPersistentAttributes().get(USER_PROGRESS)));
        this.userProgress = userProgress != null ? userProgress : new UserProgress();
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        if (getSessionAttributes().containsKey(BLOCKED_ACTIVITY_CALL)) {
            List<PhraseContainer> dialog = regularPhraseManager.getValueByKey(PhraseConstants.SELECT_BLOCKED_ACTIVTY);
            builder.addResponse(dialogTranslator.translate(dialog, true));
            getSessionAttributes().remove(BLOCKED_ACTIVITY_CALL);
        }

        activitySelectionAppender.appendWithSelection(builder, userProgress, getSessionAttributes());

        return builder.build();
    }
}
