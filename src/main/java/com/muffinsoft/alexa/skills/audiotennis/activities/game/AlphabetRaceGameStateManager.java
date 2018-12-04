package com.muffinsoft.alexa.skills.audiotennis.activities.game;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.WordContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class AlphabetRaceGameStateManager extends CompetitionGameStateManager {

    public AlphabetRaceGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.ALPHABET_RACE;
    }

    @Override
    protected boolean isSuccessAnswer() {

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
            char firstEnemyLetter = previousWord.charAt(0);
            char nextLetter = activityManager.getNextLetter(firstEnemyLetter);

            this.characterWithMistake = nextLetter;

            String userReply = getUserReply().toLowerCase();
            char firstPlayerLetter = userReply.charAt(0);

            if (!Objects.equals(nextLetter, firstPlayerLetter)) {
                return false;
            }

            return !isWordAlreadyUser();
        }
        return false;
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char firstLetter = getUserReply().charAt(0);
        char wrongLetter = activityManager.getRandomLetterExcept(firstLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.addUsedWord(word);
        return word;
    }

    @Override
    protected String getAlreadyUsedWordByActivityRules() {
        char firstLetter = getUserReply().charAt(0);
        char nextLetter = activityManager.getNextLetter(firstLetter);
        Set<String> usedWords = this.activityProgress.getUsedWords();
        List<String> usedWordsOnLetter = new ArrayList<>();
        for (String word : usedWords) {
            if (word.startsWith(String.valueOf(nextLetter))) {
                usedWordsOnLetter.add(word);
            }
        }
        if(usedWordsOnLetter.isEmpty()) {
            return null;
        }
        return usedWordsOnLetter.get(ThreadLocalRandom.current().nextInt(usedWordsOnLetter.size()));
    }

    @Override
    protected String getNextRightWordForActivity() {
        char firstLetter = getUserReply().charAt(0);
        char nextLetter = activityManager.getNextLetter(firstLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(nextLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.addUsedWord(word);
        return word;
    }
}