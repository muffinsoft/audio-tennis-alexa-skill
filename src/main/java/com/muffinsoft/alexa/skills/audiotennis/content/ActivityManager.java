package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivitySettings;
import com.muffinsoft.alexa.skills.audiotennis.models.DictionaryManager;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Collections.emptySet;

public class ActivityManager {

    private static final String ALPHABET_RACE = "settings/alphabet-race.json";
    private static final String LAST_LETTER = "settings/last-letter.json";
    private static final String ONOMATOPOEIA = "settings/bam-wham.json";
    private static final String RHYME_MATCH = "settings/rhyme-match.json";

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

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
        containerByActivity.put(ActivityType.BAM_WHAM, contentLoader.loadContent(new ActivitySettings(), ONOMATOPOEIA, new TypeReference<ActivitySettings>() {
        }));
        containerByActivity.put(ActivityType.RHYME_MATCH, contentLoader.loadContent(new ActivitySettings(), RHYME_MATCH, new TypeReference<ActivitySettings>() {
        }));
    }

    public ActivitySettings getSettingsForActivity(ActivityType type) {
        return containerByActivity.get(type);
    }

    public WordContainer getRandomWordForActivity(ActivityType activityType) {
        return getRandomWordForActivity(activityType, emptySet());
    }

    public WordContainer getRandomWordForActivity(ActivityType activityType, Set<String> usedWords) {
        if (activityType == ActivityType.BAM_WHAM) {
            ActivitySettings activitySettings = containerByActivity.get(activityType);
            Map<String, String> wordsToReactions = activitySettings.getWordsToReactions();

            Set<String> strings = new HashSet<>(wordsToReactions.keySet());
            strings.removeAll(usedWords);
            return getWordWithRhyme(wordsToReactions, strings);
        }
        else if (activityType == ActivityType.RHYME_MATCH) {
            return getRandomWordForRhymeMatchActivityFromLetter(usedWords);
        }
        else {
            char character = getRandomCharFromString(alphabet);
            return getRandomWordForCompetitionActivityFromLetter(character, usedWords);
        }
    }

    private WordContainer getWordWithRhyme(Map<String, String> wordsToReactions, Set<String> strings) {
        if (strings.isEmpty()) {
            return WordContainer.empty();
        }
        String word = getRandomWordFromCollection(strings);
        String userReaction = wordsToReactions.get(word);
        return new WordContainer(word, userReaction);
    }

    private WordContainer getRandomWordForRhymeMatchActivityFromLetter(Set<String> usedWords) {
        Map<String, String> activityWords = dictionaryManager.getForRhymeMathActivity();

        HashSet<String> words = new HashSet<>(activityWords.keySet());
        words.removeAll(usedWords);

        return getWordWithRhyme(activityWords, words);
    }

    private char getRandomCharFromString(String input) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int index = random.nextInt(input.length());
        return input.charAt(index);
    }

    public char getRandomLetterExcept(char lastLetter) {
        String replace = alphabet.replace(String.valueOf(lastLetter), "");
        return getRandomCharFromString(replace);
    }

    public boolean isKnownWord(List<String> inputWords) {
        if (inputWords == null || inputWords.isEmpty()) {
            return false;
        }
        for (String inputWord : inputWords) {
            String word = inputWord.toLowerCase().trim();
            Map<Character, HashSet<String>> totalWords = dictionaryManager.getTotalWordsDictionary();
            char firstChar = word.charAt(0);
            HashSet<String> wordsFromLetter = totalWords.get(firstChar);
            if (wordsFromLetter.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public WordContainer getRandomWordForCompetitionActivityFromLetter(char lastLetter, Set<String> usedWords) {
        Map<Character, HashSet<String>> activityWords = dictionaryManager.getForCompetitionActivity();

        Set<String> wordsByLetter = activityWords.get(lastLetter);

        if(wordsByLetter == null) {
            return WordContainer.empty();
        }

        HashSet<String> wordsByRule = new HashSet<>(wordsByLetter);
        wordsByRule.removeAll(usedWords);

        if (wordsByRule.isEmpty()) {
            return WordContainer.empty();
        }

        String word = getRandomWordFromCollection(wordsByRule);

        return new WordContainer(word);
    }

    private String getRandomWordFromCollection(Set<String> words) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return words.stream()
                .skip(random.nextInt(words.size()))
                .findFirst()
                .orElse(null);
    }

    public char getNextLetter(char letter) {
        int index = alphabet.indexOf(letter);
        int nextIndex = index + 1;
        if (nextIndex > alphabet.length() - 1) {
            nextIndex = 0;
        }
        return alphabet.charAt(nextIndex);
    }

    public String findRhymeForWord(String word) {
        Map<String, String> forRhymeMathActivity = dictionaryManager.getAllWordsWithRhymes();
        return forRhymeMathActivity.get(word.toLowerCase());
    }
}
