package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.sdk.components.BaseDialogTranslator;
import com.muffinsoft.alexa.skills.audiotennis.components.ActivitySelectionAppender;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;

public class SettingsDependencyContainer {

    private final CardManager cardManager;
    private final UserReplyManager userReplyManager;
    private final AliasManager aliasManager;
    private final BaseDialogTranslator dialogTranslator;
    private final ActivityManager activityManager;
    private final ProgressManager progressManager;
    private final ActivitySelectionAppender activitySelectionAppender;

    public SettingsDependencyContainer(CardManager cardManager, UserReplyManager userReplyManager, AliasManager aliasManager, BaseDialogTranslator dialogTranslator, ActivityManager activityManager, ProgressManager progressManager, ActivitySelectionAppender activitySelectionAppender) {
        this.cardManager = cardManager;
        this.userReplyManager = userReplyManager;
        this.aliasManager = aliasManager;
        this.dialogTranslator = dialogTranslator;
        this.activityManager = activityManager;
        this.progressManager = progressManager;
        this.activitySelectionAppender = activitySelectionAppender;
    }

    public ProgressManager getProgressManager() {
        return progressManager;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public UserReplyManager getUserReplyManager() {
        return userReplyManager;
    }

    public AliasManager getAliasManager() {
        return aliasManager;
    }

    public BaseDialogTranslator getDialogTranslator() {
        return dialogTranslator;
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }

    public ActivitySelectionAppender getActivitySelectionAppender() {
        return activitySelectionAppender;
    }
}
