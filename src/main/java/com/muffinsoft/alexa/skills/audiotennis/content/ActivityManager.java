package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivitySettings;
import com.muffinsoft.alexa.skills.audiotennis.models.DictionaryManager;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityManager {

    private static final String ALPHABET_RACE = "settings/alphabet-race.json";
    private static final String LAST_LETTER = "settings/last-letter.json";
    private static final String ONOMATOPOEIA = "settings/onomatopoeia.json";
    private static final String RHYME_MATCH = "settings/rhyme-match.json";

    private static final String alphabet = "abcdefjhijklmnopqrstuvwxyz";

    private final DictionaryManager dictionaryManager;

    private final Map<ActivityType, ActivitySettings> containerByActivity;

    public ActivityManager(DictionaryManager dictionaryManager) {

        this.dictionaryManager = dictionaryManager;

        containerByActivity = new HashMap<>();

        ContentLoader contentLoader = new ContentLoader(new ObjectMapper());
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

    public WordContainer getRandomWordForActivity(ActivityType activityType) {
        char character = getRandomCharFromString(alphabet);
        return getRandomWordForActivityFromLetter(activityType, character);
    }

    private char getRandomCharFromString(String input) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int index = random.nextInt(input.length());
        return alphabet.charAt(index);
    }

    public char getRandomLetterExcept(char lastLetter) {
        String replace = alphabet.replace(String.valueOf(lastLetter), "");
        return getRandomCharFromString(replace);
    }

    public WordContainer getRandomWordForActivityFromLetter(ActivityType activityType, char lastLetter) {
        return getRandomWordForActivityFromLetter(activityType, lastLetter, Collections.emptySet());
    }

    public WordContainer getRandomWordForActivityFromLetter(ActivityType activityType, char lastLetter, Set<String> usedWords) {
        String word;
        do {
            Map<Character, HashSet<String>> activityWords = dictionaryManager.getForActivity(activityType);

            HashSet<String> wordsByRule = activityWords.get(lastLetter);

            word = getRandomWordFromCollection(wordsByRule);
        }
        while (usedWords.contains(word));

        return new WordContainer(word);
    }

    private String getRandomWordFromCollection(Set<String> words) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return words.stream()
                .skip(random.nextInt(words.size()))
                .findFirst()
                .orElse(null);
    }

    public boolean isWordAvailableForActivity(String word) {
        return false;
    }
}
