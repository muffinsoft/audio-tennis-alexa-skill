package com.muffinsoft.alexa.skills.audiotennis;


import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.skills.audiotennis.components.TennisIntentFabric;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

public class IoC {

    private static final PhraseManager phraseManager;
    private static final CardManager cardManager;
    private static final UserReplyManager userReplyManager;
    private static final ProgressManager progressManager;
    private static final AliasManager aliasManager;
    private static final ConfigContainer configContainer;
    private static final TennisIntentFabric intentFabric;

    static {
        phraseManager = new PhraseManager("phrases/en-US.json");
        cardManager = new CardManager("phrases/cards.json");
        userReplyManager = new UserReplyManager("phrases/replies.json");
        aliasManager = new AliasManager("settings/aliases.json");
        progressManager = new ProgressManager("settings/defaults.json");
        configContainer = new ConfigContainer(phraseManager, cardManager, userReplyManager, aliasManager);
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
