package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProgress {

    public UserProgress() {
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "class UserProgress {}";
    }
}
