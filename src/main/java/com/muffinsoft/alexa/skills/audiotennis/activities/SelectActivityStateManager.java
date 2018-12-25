package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.ActivitySelectionAppender;
import com.muffinsoft.alexa.skills.audiotennis.components.UserProgressConverter;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;

public class SelectActivityStateManager extends BaseStateManager {

    private final ActivitySelectionAppender activitySelectionAppender;

    private UserProgress userProgress;

    public SelectActivityStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.activitySelectionAppender = settingsDependencyContainer.getActivitySelectionAppender();
    }

    @Override
    protected void populateActivityVariables() {
        UserProgress userProgress = UserProgressConverter.fromJson(String.valueOf(getPersistentAttributes().get(USER_PROGRESS)));
        this.userProgress = userProgress != null ? userProgress : new UserProgress();
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        activitySelectionAppender.append(builder, userProgress);

        return builder.build();
    }
}
