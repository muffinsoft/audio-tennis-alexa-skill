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

    private final Map<Character, HashSet<String>> competitionsVocabulary = new HashMap<>();
    private final Map<String, String> wordToRhymes = new HashMap<>();

    public DictionaryManager() {

        DictionaryFileLoader dictionaryFileLoader = new DictionaryFileLoader();

        try {
            Set<String> words = dictionaryFileLoader.uploadCollection(LETTER_AND_ALPHABET_WORDS_TXT);
            for (String word : words) {
                char firstLetter = word.charAt(0);
                if (!competitionsVocabulary.containsKey(firstLetter)) {
                    competitionsVocabulary.put(firstLetter, new HashSet<>());
                }
                competitionsVocabulary.get(firstLetter).add(word);
            }
            Map<String, String> wordsAndRhymes = dictionaryFileLoader.uploadMap(RHYME_MATCH_WORDS_CSV);
            for (Map.Entry<String, String> entry : wordsAndRhymes.entrySet()) {
                String rhyme = entry.getValue().replace("-", "").trim();
                String word = entry.getKey().trim();
                wordToRhymes.put(word, rhyme);
            }
            logger.info("Dictionaries filled with words: competitions - " + words.size() + "; rhymes - " + wordToRhymes.size());
        }
        catch (IOException e) {
            System.exit(1);
        }
    }

    public Map<Character, HashSet<String>> getForCompetitionActivity() {
        return competitionsVocabulary;
    }


    public Map<String, String> getForRhymeMathActivity() {
        return wordToRhymes;
    }
}
