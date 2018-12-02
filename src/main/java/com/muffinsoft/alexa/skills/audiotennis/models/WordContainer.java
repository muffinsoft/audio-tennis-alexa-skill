package com.muffinsoft.alexa.skills.audiotennis.models;

public class WordContainer {

    private final String word;
    private final String userReaction;
    private final boolean isEmpty;

    private WordContainer(boolean isEmpty) {
        this.isEmpty = isEmpty;
        this.word = null;
        this.userReaction = null;
    }

    public WordContainer(String word) {
        this.isEmpty = false;
        this.word = word;
        this.userReaction = null;
    }

    public WordContainer(String word, String userReaction) {
        this.isEmpty = false;
        this.word = word;
        this.userReaction = userReaction;
    }

    public static WordContainer empty() {
        return new WordContainer(true);
    }

    public String getWord() {
        return word;
    }

    public String getUserReaction() {
        return userReaction;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
