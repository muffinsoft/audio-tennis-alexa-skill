package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityPhrases;

import java.util.HashMap;
import java.util.Map;

public class ActivitiesPhraseManager {

    private static final String ALPHABET_RACE = "phrases/alphabet-race-phrases.json";
    private static final String LAST_LETTER = "phrases/last-letter-phrases.json";
    private static final String ONOMATOPOEIA = "phrases/onomatopoeia-phrases.json";
    private static final String RHYME_MATCH = "phrases/rhyme-match-phrases.json";

    private final ContentLoader contentLoader = new ContentLoader(new ObjectMapper());

    private final Map<ActivityType, ActivityPhrases> containerByActivity;

    public ActivitiesPhraseManager() {

        containerByActivity = new HashMap<>();

        containerByActivity.put(ActivityType.ALPHABET_RACE, contentLoader.loadContent(new ActivityPhrases(), ALPHABET_RACE, new TypeReference<ActivityPhrases>() {
        }));
        containerByActivity.put(ActivityType.LAST_LETTER, contentLoader.loadContent(new ActivityPhrases(), LAST_LETTER, new TypeReference<ActivityPhrases>() {
        }));
        containerByActivity.put(ActivityType.ONOMATOPOEIA, contentLoader.loadContent(new ActivityPhrases(), ONOMATOPOEIA, new TypeReference<ActivityPhrases>() {
        }));
        containerByActivity.put(ActivityType.RHYME_MATCH, contentLoader.loadContent(new ActivityPhrases(), RHYME_MATCH, new TypeReference<ActivityPhrases>() {
        }));
    }

    public ActivityPhrases getPhrasesForActivity(ActivityType type) {
        return containerByActivity.get(type);
    }
}
