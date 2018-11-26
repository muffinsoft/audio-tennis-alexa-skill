package com.muffinsoft.alexa.skills.audiotennis.models;

import java.util.Collections;
import java.util.List;

public class WordContainer {

    private final String word;
    private final List<String> userReply;

    public WordContainer(String word, List<String> userReply) {
        this.word = word;
        this.userReply = userReply;
    }

    public WordContainer(String word, String userReply) {
        this.word = word;
        this.userReply = Collections.singletonList(userReply);
    }

    public String getWord() {
        return word;
    }

    public List<String> getUserReply() {
        return userReply;
    }
}
