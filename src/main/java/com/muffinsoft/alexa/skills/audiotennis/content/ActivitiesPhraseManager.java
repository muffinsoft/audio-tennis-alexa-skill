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
    private static final String BAM_WHAM = "phrases/bam-wham-phrases.json";
    private static final String RHYME_MATCH = "phrases/rhyme-match-phrases.json";

    private final ContentLoader contentLoader = new ContentLoader(new ObjectMapper());

    private final Map<ActivityType, ActivityPhrases> containerByActivity;

    public ActivitiesPhraseManager() {

        containerByActivity = new HashMap<>();

        containerByActivity.put(ActivityType.ALPHABET_RACE, contentLoader.loadContent(new ActivityPhrases(), ALPHABET_RACE, new TypeReference<ActivityPhrases>() {
        }));
        containerByActivity.put(ActivityType.LAST_LETTER, contentLoader.loadContent(new ActivityPhrases(), LAST_LETTER, new TypeReference<ActivityPhrases>() {
        }));
        containerByActivity.put(ActivityType.BAM_WHAM, contentLoader.loadContent(new ActivityPhrases(), BAM_WHAM, new TypeReference<ActivityPhrases>() {
        }));
        containerByActivity.put(ActivityType.RHYME_MATCH, contentLoader.loadContent(new ActivityPhrases(), RHYME_MATCH, new TypeReference<ActivityPhrases>() {
        }));
    }

    public ActivityPhrases getPhrasesForActivity(ActivityType type) {
        return containerByActivity.get(type);
    }
}
