package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;

public class ConfigContainer {

    private final PhraseManager phraseManager;
    private final CardManager cardManager;
    private final UserReplyManager userReplyManager;
    private final AliasManager aliasManager;

    public ConfigContainer(PhraseManager phraseManager, CardManager cardManager, UserReplyManager userReplyManager, AliasManager aliasManager) {
        this.phraseManager = phraseManager;
        this.cardManager = cardManager;
        this.userReplyManager = userReplyManager;
        this.aliasManager = aliasManager;
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
}
