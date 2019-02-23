package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;

import java.util.HashMap;
import java.util.Map;

public class AplManager {

    private final static String APL_IMAGE = "settings/apl-image.json";
    private final static String APL_SCORES = "settings/apl-scores.json";

    private final ContentLoader contentLoader = new ContentLoader(new ObjectMapper());

    private final Map<String, Map<String, Object>> imageDocument;
    private final Map<String, Map<String, Object>> scoreDocument;

    public AplManager() {
        imageDocument = contentLoader.loadContent(new HashMap<>(), APL_IMAGE, new TypeReference<Map<String, Map<String, Object>>>() {
        });
        scoreDocument = contentLoader.loadContent(new HashMap<>(), APL_SCORES, new TypeReference<Map<String, Map<String, Object>>>() {
        });
    }

    public Map<String, Map<String, Object>> getImageDocument() {
        return imageDocument;
    }

    public Map<String, Map<String, Object>> getScoreDocument() {
        return scoreDocument;
    }
}
