package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivitySettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityManager {

    private static final String ALPHABET_RACE = "settings/alphabet-race.json";
    private static final String LAST_LETTER = "settings/last-letter.json";
    private static final String ONOMATOPOEIA = "settings/onomatopoeia.json";
    private static final String RHYME_MATCH = "settings/rhyme-match.json";

    private final ContentLoader contentLoader = new ContentLoader(new ObjectMapper());

    private final Map<ActivityType, ActivitySettings> containerByActivity;

    private final Map<String, Map<String, List<String>>> vocabularies = new HashMap<>();

    public ActivityManager() {

        containerByActivity = new HashMap<>();

        containerByActivity.put(ActivityType.ALPHABET_RACE, contentLoader.loadContent(new ActivitySettings(), ALPHABET_RACE, new TypeReference<ActivitySettings>() {
        }));
        containerByActivity.put(ActivityType.LAST_LETTER, contentLoader.loadContent(new ActivitySettings(), LAST_LETTER, new TypeReference<ActivitySettings>() {
        }));
        containerByActivity.put(ActivityType.ONOMATOPOEIA, contentLoader.loadContent(new ActivitySettings(), ONOMATOPOEIA, new TypeReference<ActivitySettings>() {
        }));
        containerByActivity.put(ActivityType.RHYME_MATCH, contentLoader.loadContent(new ActivitySettings(), RHYME_MATCH, new TypeReference<ActivitySettings>() {
        }));
    }

    public ActivitySettings getSettingsForActivity(ActivityType type) {
        return containerByActivity.get(type);
    }

}
