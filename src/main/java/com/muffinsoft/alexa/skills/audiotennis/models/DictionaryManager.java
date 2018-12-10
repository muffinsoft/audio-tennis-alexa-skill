package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.components.DictionaryFileLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DictionaryManager {

    protected static final Logger logger = LogManager.getLogger(DictionaryManager.class);

    private static final String LETTER_AND_ALPHABET_WORDS_TXT = "settings/vocabularies/lastLetterAndAlphabetWords.txt";
    private static final String RHYME_MATCH_WORDS_CSV = "settings/vocabularies/rhymeMatchWords.csv";

    private final Map<Character, HashSet<String>> enemyCompetitionDictionary = new HashMap<>();
    private final Map<Character, HashSet<String>> totalWordsDictionary = new HashMap<>();
    private final Map<String, String> wordToRhymesDictionary = new HashMap<>();

    public DictionaryManager() {

        DictionaryFileLoader dictionaryFileLoader = new DictionaryFileLoader();

        try {

            Set<String> wordsFromFile = dictionaryFileLoader.uploadCollection(LETTER_AND_ALPHABET_WORDS_TXT);

            for (String rawWord : wordsFromFile) {
                String word = rawWord.toLowerCase().trim().replace(" ", "");
                char firstLetter = word.charAt(0);
                if (!enemyCompetitionDictionary.containsKey(firstLetter)) {
                    enemyCompetitionDictionary.put(firstLetter, new HashSet<>());
                }
                if (!totalWordsDictionary.containsKey(firstLetter)) {
                    totalWordsDictionary.put(firstLetter, new HashSet<>());
                }
                enemyCompetitionDictionary.get(firstLetter).add(word);
                totalWordsDictionary.get(firstLetter).add(word);
            }

            Map<String, String> rhymesFromFile = dictionaryFileLoader.uploadMap(RHYME_MATCH_WORDS_CSV);
            for (Map.Entry<String, String> entry : rhymesFromFile.entrySet()) {
                String rhyme = entry.getValue().replace("-", "").trim();
                String word = entry.getKey().toLowerCase().trim().replace(" ", "");
                wordToRhymesDictionary.put(word, rhyme);

                char firstLetter = word.toLowerCase().charAt(0);
                if (!totalWordsDictionary.containsKey(firstLetter)) {
                    totalWordsDictionary.put(firstLetter, new HashSet<>());
                }
                totalWordsDictionary.get(firstLetter).add(word);
            }
        }
        catch (IOException e) {
            System.exit(1);
        }

        logger.info("Dictionaries filled with " + this.totalWordsDictionary.entrySet().stream()
                .mapToLong(characterHashSetEntry -> characterHashSetEntry.getValue().size())
                .sum() + " words");
    }

    public Map<Character, HashSet<String>> getForCompetitionActivity() {
        return enemyCompetitionDictionary;
    }

    public Map<Character, HashSet<String>> getTotalWordsDictionary() {
        return totalWordsDictionary;
    }

    public Map<String, String> getForRhymeMathActivity() {
        return wordToRhymesDictionary;
    }
}
