package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ProgressContainer;

public class ProgressManager {

    private ProgressContainer container;
    private ObjectMapper objectMapper;

    public ProgressManager(String path) {
        this.objectMapper = new ObjectMapper();
        this.container = new ContentLoader(objectMapper).loadContent(new ProgressContainer(), path, new TypeReference<ProgressContainer>() {
        });
    }

    public ActivityType getFirstActivity() {
        return ActivityType.valueOf(container.getDefaultActivity());
    }
}
