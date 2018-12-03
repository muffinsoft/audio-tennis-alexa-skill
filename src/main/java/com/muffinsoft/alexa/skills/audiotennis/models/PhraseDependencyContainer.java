package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GeneralActivityPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;

public class PhraseDependencyContainer {

    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final GeneralActivityPhraseManager generalActivityPhraseManager;

    public PhraseDependencyContainer(RegularPhraseManager regularPhraseManager, ActivitiesPhraseManager activitiesPhraseManager, GeneralActivityPhraseManager generalActivityPhraseManager) {
        this.regularPhraseManager = regularPhraseManager;
        this.activitiesPhraseManager = activitiesPhraseManager;
        this.generalActivityPhraseManager = generalActivityPhraseManager;
    }

    public GeneralActivityPhraseManager getGeneralActivityPhraseManager() {
        return generalActivityPhraseManager;
    }

    public RegularPhraseManager getRegularPhraseManager() {
        return regularPhraseManager;
    }

    public ActivitiesPhraseManager getActivitiesPhraseManager() {
        return activitiesPhraseManager;
    }
}
