package com.muffinsoft.alexa.skills.audiotennis;


import com.muffinsoft.alexa.sdk.components.BaseDialogTranslator;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.skills.audiotennis.components.TennisIntentFabric;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ActivityManager;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.CharactersManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GeneralActivityPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.ProgressManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.UserReplyManager;
import com.muffinsoft.alexa.skills.audiotennis.models.DictionaryManager;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;

public class IoC {

    private static final RegularPhraseManager REGULAR_PHRASE_MANAGER;
    private static final ActivitiesPhraseManager ACTIVITIES_PHRASE_MANAGER;
    private static final CardManager CARD_MANAGER;
    private static final UserReplyManager USER_REPLY_MANAGER;
    private static final ProgressManager PROGRESS_MANAGER;
    private static final AliasManager ALIAS_MANAGER;
    private static final GreetingsPhraseManager GREETINGS_PHRASE_MANAGER;
    private static final SettingsDependencyContainer SETTINGS_DEPENDENCY_CONTAINER;
    private static final PhraseDependencyContainer PHRASE_DEPENDENCY_CONTAINER;
    private static final TennisIntentFabric INTENT_FABRIC;
    private static final BaseDialogTranslator BASE_DIALOG_TRANSLATOR;
    private static final GeneralActivityPhraseManager GENERAL_ACTIVITY_PHRASE_MANAGER;
    private static final CharactersManager CHARACTERS_MANAGER;
    private static final ActivityManager ACTIVITY_MANAGER;
    private static final DictionaryManager DICTIONARY_MANAGER;

    static {
        REGULAR_PHRASE_MANAGER = new RegularPhraseManager("phrases/en-US.json");
        CARD_MANAGER = new CardManager("phrases/cards.json");
        USER_REPLY_MANAGER = new UserReplyManager("settings/replies.json");
        ALIAS_MANAGER = new AliasManager("settings/aliases.json");
        PROGRESS_MANAGER = new ProgressManager("settings/defaults.json");
        GREETINGS_PHRASE_MANAGER = new GreetingsPhraseManager("phrases/greetings-phrases.json");
        CHARACTERS_MANAGER = new CharactersManager("phrases/characters.json");
        BASE_DIALOG_TRANSLATOR = new BaseDialogTranslator(CHARACTERS_MANAGER.getContainer());
        ACTIVITIES_PHRASE_MANAGER = new ActivitiesPhraseManager();
        GENERAL_ACTIVITY_PHRASE_MANAGER = new GeneralActivityPhraseManager();
        DICTIONARY_MANAGER = new DictionaryManager();
        ACTIVITY_MANAGER = new ActivityManager(DICTIONARY_MANAGER);
        PHRASE_DEPENDENCY_CONTAINER = new PhraseDependencyContainer(REGULAR_PHRASE_MANAGER, GREETINGS_PHRASE_MANAGER, ACTIVITIES_PHRASE_MANAGER, GENERAL_ACTIVITY_PHRASE_MANAGER);
        SETTINGS_DEPENDENCY_CONTAINER = new SettingsDependencyContainer(CARD_MANAGER, USER_REPLY_MANAGER, ALIAS_MANAGER, BASE_DIALOG_TRANSLATOR, ACTIVITY_MANAGER);
        INTENT_FABRIC = new TennisIntentFabric(SETTINGS_DEPENDENCY_CONTAINER, PHRASE_DEPENDENCY_CONTAINER);
    }

    public static UserReplyManager provideUserReplyManager() {
        return USER_REPLY_MANAGER;
    }

    public static ProgressManager provideProgressManager() {
        return PROGRESS_MANAGER;
    }

    public static SettingsDependencyContainer provideSettingsDependencyContainer() {
        return SETTINGS_DEPENDENCY_CONTAINER;
    }

    public static PhraseDependencyContainer providePhraseDependencyContainer() {
        return PHRASE_DEPENDENCY_CONTAINER;
    }

    public static IntentFactory provideIntentFactory() {
        return INTENT_FABRIC;
    }
}