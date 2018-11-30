package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.components.DictionaryFileLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.ALPHABET_RACE;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.LAST_LETTER;

public class DictionaryManager {

    private static final String LETTER_AND_ALPHABET_WORDS_TXT = "settings/vocabularies/lastLetterAndAlphabetWords.txt";

    private final Map<ActivityType, Map<Character, HashSet<String>>> vocabularies = new HashMap<>();

    public DictionaryManager() {

        DictionaryFileLoader dictionaryFileLoader = new DictionaryFileLoader();

        try {
            Set<String> words = dictionaryFileLoader.upload(LETTER_AND_ALPHABET_WORDS_TXT);
            for (String word : words) {
                char firstLetter = word.charAt(0);
                if (!vocabularies.containsKey(ALPHABET_RACE)) {
                    vocabularies.put(ALPHABET_RACE, new HashMap<>());
                }
                Map<Character, HashSet<String>> alphabetRaceVocabulary = vocabularies.get(ALPHABET_RACE);
                if (!alphabetRaceVocabulary.containsKey(firstLetter)) {
                    alphabetRaceVocabulary.put(firstLetter, new HashSet<>());
                }
                alphabetRaceVocabulary.get(firstLetter).add(word);

                char lastLetter = word.charAt(0); //TODO this is first letter
                if (!vocabularies.containsKey(LAST_LETTER)) {
                    vocabularies.put(LAST_LETTER, new HashMap<>());
                }
                Map<Character, HashSet<String>> lastLetterVocabulary = vocabularies.get(LAST_LETTER);
                if (!lastLetterVocabulary.containsKey(lastLetter)) {
                    lastLetterVocabulary.put(lastLetter, new HashSet<>());
                }
                lastLetterVocabulary.get(lastLetter).add(word);
            }
        }
        catch (IOException e) {
            System.exit(1);
        }
    }

    public Map<Character, HashSet<String>> getForActivity(ActivityType type) {
        return vocabularies.get(type);
    }
}
