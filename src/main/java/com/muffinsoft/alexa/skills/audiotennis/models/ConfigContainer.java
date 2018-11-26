package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.sdk.components.BaseDialogTranslator;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;

public class ConfigContainer {

    private final RegularPhraseManager regularPhraseManager;
    private final CardManager cardManager;
    private final UserReplyManager userReplyManager;
    private final AliasManager aliasManager;
    private final GreetingsManager greetingsManager;
    private final BaseDialogTranslator dialogTranslator;
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final ActivityManager activityManager;

    public ConfigContainer(RegularPhraseManager regularPhraseManager, CardManager cardManager, UserReplyManager userReplyManager, AliasManager aliasManager, GreetingsManager greetingsManager, BaseDialogTranslator dialogTranslator, ActivitiesPhraseManager activitiesPhraseManager, ActivityManager activityManager) {
        this.regularPhraseManager = regularPhraseManager;
        this.cardManager = cardManager;
        this.userReplyManager = userReplyManager;
        this.aliasManager = aliasManager;
        this.greetingsManager = greetingsManager;
        this.dialogTranslator = dialogTranslator;
        this.activitiesPhraseManager = activitiesPhraseManager;
        this.activityManager = activityManager;
    }

    public RegularPhraseManager getRegularPhraseManager() {
        return regularPhraseManager;
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

    public GreetingsManager getGreetingsManager() {
        return greetingsManager;
    }

    public BaseDialogTranslator getDialogTranslator() {
        return dialogTranslator;
    }

    public ActivitiesPhraseManager getActivitiesPhraseManager() {
        return activitiesPhraseManager;
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }
}
