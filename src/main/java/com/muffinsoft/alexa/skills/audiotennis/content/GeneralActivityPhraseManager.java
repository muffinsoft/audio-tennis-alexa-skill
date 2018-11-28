package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.models.GeneralActivityPhrases;

public class GeneralActivityPhraseManager {

    private static final String GENERAL_ACTIVITY_PHRASES = "phrases/general-activity-phrases.json";

    private final GeneralActivityPhrases generalActivityPhrases;

    public GeneralActivityPhraseManager() {
        ContentLoader contentLoader = new ContentLoader(new ObjectMapper());
        generalActivityPhrases = contentLoader.loadContent(new GeneralActivityPhrases(), GENERAL_ACTIVITY_PHRASES, new TypeReference<GeneralActivityPhrases>() {
        });
    }

    public GeneralActivityPhrases getGeneralActivityPhrases() {
        return generalActivityPhrases;
    }
}
