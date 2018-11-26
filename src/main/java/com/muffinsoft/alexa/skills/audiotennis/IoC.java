package com.muffinsoft.alexa.skills.audiotennis;


import com.muffinsoft.alexa.sdk.components.BaseDialogTranslator;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.skills.audiotennis.components.TennisIntentFabric;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CharactersManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

public class IoC {

    private static final RegularPhraseManager regularPhraseManager;
    private static final ActivitiesPhraseManager activitiesPhraseManager;
    private static final CardManager cardManager;
    private static final UserReplyManager userReplyManager;
    private static final ProgressManager progressManager;
    private static final AliasManager aliasManager;
    private static final GreetingsManager greetingsManager;
    private static final ConfigContainer configContainer;
    private static final TennisIntentFabric intentFabric;
    private static final BaseDialogTranslator dialogTranslator;
    private static final CharactersManager charactersManager;
    private static final ActivityManager activityManager;

    static {
        regularPhraseManager = new RegularPhraseManager("phrases/en-US.json");
        cardManager = new CardManager("phrases/cards.json");
        userReplyManager = new UserReplyManager("phrases/replies.json");
        aliasManager = new AliasManager("settings/aliases.json");
        progressManager = new ProgressManager("settings/defaults.json");
        greetingsManager = new GreetingsManager("phrases/greetings.json");
        charactersManager = new CharactersManager("phrases/characters.json");
        dialogTranslator = new BaseDialogTranslator(charactersManager.getContainer());
        activitiesPhraseManager = new ActivitiesPhraseManager();
        activityManager = new ActivityManager();
        configContainer = new ConfigContainer(regularPhraseManager, cardManager, userReplyManager, aliasManager, greetingsManager, dialogTranslator, activitiesPhraseManager, activityManager);
        intentFabric = new TennisIntentFabric(configContainer);
    }

    public static UserReplyManager provideUserReplyManager() {
        return userReplyManager;
    }

    public static ProgressManager provideProgressManager() {
        return progressManager;
    }

    public static ConfigContainer provideConfigurationContainer() {
        return configContainer;
    }

    public static IntentFactory provideIntentFactory() {
        return intentFabric;
    }
}
