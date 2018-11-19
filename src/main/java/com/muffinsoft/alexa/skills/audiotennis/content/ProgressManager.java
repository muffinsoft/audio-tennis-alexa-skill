package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ProgressContainer;

public class ProgressManager {

    private ProgressContainer container;

    public ProgressManager(String path) {
        this.container = new ContentLoader().loadContent(new ProgressContainer(), path, new TypeReference<ProgressContainer>() {
        });
    }

    public ActivityType getFirstActivity() {
        return ActivityType.valueOf(container.getDefaultActivity());
    }
}
