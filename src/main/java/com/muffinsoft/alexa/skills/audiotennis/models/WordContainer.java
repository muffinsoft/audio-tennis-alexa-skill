package com.muffinsoft.alexa.skills.audiotennis.models;

public class WordContainer {

    private final String word;
    private final String userReaction;

    public WordContainer(String word) {
        this.word = word;
        this.userReaction = null;
    }

    public WordContainer(String word, String userReaction) {
        this.word = word;
        this.userReaction = userReaction;
    }

    public String getWord() {
        return word;
    }

    public String getUserReaction() {
        return userReaction;
    }
}
