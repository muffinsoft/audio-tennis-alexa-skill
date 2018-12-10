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

public class LastLetterGameStateManager extends CompetitionGameStateManager {

    public LastLetterGameStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(inputSlots, attributesManager, settingsDependencyContainer, phraseDependencyContainer);
        this.currentActivityType = ActivityType.LAST_LETTER;
    }

    @Override
    protected void appendDynamicEntities(DialogItem.Builder builder) {
        String previousWord = this.activityProgress.getPreviousWord();
        if (previousWord != null && !previousWord.isEmpty()) {
            char nextLetter = previousWord.charAt(previousWord.length() - 1);
            Set<String> words = activityManager.getAllWordsFromLetter(nextLetter);
            builder.withDynamicEntities(words);
        }
    }

    @Override
    protected boolean isSuccessAnswer() {

        boolean isSuccess = false;

        if (getUserMultipleReplies().isEmpty()) {

            String previousWord = this.activityProgress.getPreviousWord().toLowerCase();
            char lastLetter = previousWord.charAt(previousWord.length() - 1);

            this.characterWithMistake = lastLetter;

            String userReply = getUserReply().toLowerCase();
            char firstLetter = userReply.charAt(0);

            if (!Objects.equals(lastLetter, firstLetter)) {
                return false;
            }

            isSuccess = !isWordAlreadyUser();
        }
        return isSuccess;
    }

    @Override
    protected String getNextWrongWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        char wrongLetter = activityManager.getRandomLetterExcept(lastLetter);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(wrongLetter, this.activityProgress.getUsedWords());
        String word = randomWordForActivityFromLetter.getWord();
        this.activityProgress.addUsedWord(word);
        return word;
    }

    @Override
    protected String getAlreadyUsedWordByActivityRules() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        Set<String> usedWords = this.activityProgress.getUsedWords();
        List<String> usedWordsOnLetter = new ArrayList<>();
        for (String word : usedWords) {
            if (word.startsWith(String.valueOf(lastLetter))) {
                usedWordsOnLetter.add(word);
            }
        }
        if (usedWordsOnLetter.isEmpty()) {
            return null;
        }
        return usedWordsOnLetter.get(ThreadLocalRandom.current().nextInt(usedWordsOnLetter.size()));
    }

    @Override
    protected Character getCharWithMistakeForEnemy() {
        return getUserReply().charAt(getUserReply().length() - 1);
    }

    @Override
    protected Character getNextReplyCharacter(String word) {
        return word.charAt(word.length() - 1);
    }

    @Override
    protected String getNextRightWordForActivity() {
        char lastLetter = getUserReply().charAt(getUserReply().length() - 1);
        WordContainer randomWordForActivityFromLetter = activityManager.getRandomWordForCompetitionActivityFromLetter(lastLetter, this.activityProgress.getUsedWords());
        if (randomWordForActivityFromLetter.isEmpty()) {
            return null;
        }
        return randomWordForActivityFromLetter.getWord();
    }
}
