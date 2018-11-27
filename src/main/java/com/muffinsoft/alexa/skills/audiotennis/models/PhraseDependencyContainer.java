package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;

public class PhraseDependencyContainer {

    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final GreetingsPhraseManager greetingsPhraseManager;

    public PhraseDependencyContainer(RegularPhraseManager regularPhraseManager, GreetingsPhraseManager greetingsPhraseManager, ActivitiesPhraseManager activitiesPhraseManager) {
        this.regularPhraseManager = regularPhraseManager;
        this.greetingsPhraseManager = greetingsPhraseManager;
        this.activitiesPhraseManager = activitiesPhraseManager;
    }

    public RegularPhraseManager getRegularPhraseManager() {
        return regularPhraseManager;
    }

    public ActivitiesPhraseManager getActivitiesPhraseManager() {
        return activitiesPhraseManager;
    }

    public GreetingsPhraseManager getGreetingsPhraseManager() {
        return greetingsPhraseManager;
    }
}
