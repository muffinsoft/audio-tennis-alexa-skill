package com.muffinsoft.alexa.skills.audiotennis.models;

public class PlayerReaction {

    private final String item;
    private final String userReply;

    public PlayerReaction(String item, String userReply) {
        this.item = item;
        this.userReply = userReply;
    }

    public String getItem() {
        return item;
    }

    public String getUserReply() {
        return userReply;
    }
}
