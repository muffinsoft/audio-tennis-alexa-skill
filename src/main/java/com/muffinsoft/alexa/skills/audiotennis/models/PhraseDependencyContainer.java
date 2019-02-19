package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.content.ActivitiesPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GeneralActivityPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.content.VariablesManager;

public class PhraseDependencyContainer {

    private final RegularPhraseManager regularPhraseManager;
    private final ActivitiesPhraseManager activitiesPhraseManager;
    private final GeneralActivityPhraseManager generalActivityPhraseManager;
    private final VariablesManager variablesManager;

    public PhraseDependencyContainer(RegularPhraseManager regularPhraseManager, ActivitiesPhraseManager activitiesPhraseManager, GeneralActivityPhraseManager generalActivityPhraseManager, VariablesManager variablesManager) {
        this.regularPhraseManager = regularPhraseManager;
        this.activitiesPhraseManager = activitiesPhraseManager;
        this.generalActivityPhraseManager = generalActivityPhraseManager;
        this.variablesManager = variablesManager;
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

    public VariablesManager getVariablesManager() {
        return variablesManager;
    }
}
