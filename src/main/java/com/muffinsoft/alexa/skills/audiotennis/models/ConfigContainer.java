package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.sdk.components.BaseDialogTranslator;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsManager;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;

public class ConfigContainer {

    private final PhraseManager phraseManager;
    private final CardManager cardManager;
    private final UserReplyManager userReplyManager;
    private final AliasManager aliasManager;
    private final GreetingsManager greetingsManager;
    private final BaseDialogTranslator dialogTranslator;

    public ConfigContainer(PhraseManager phraseManager, CardManager cardManager, UserReplyManager userReplyManager, AliasManager aliasManager, GreetingsManager greetingsManager, BaseDialogTranslator dialogTranslator) {
        this.phraseManager = phraseManager;
        this.cardManager = cardManager;
        this.userReplyManager = userReplyManager;
        this.aliasManager = aliasManager;
        this.greetingsManager = greetingsManager;
        this.dialogTranslator = dialogTranslator;
    }

    public PhraseManager getPhraseManager() {
        return phraseManager;
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
}
