package com.muffinsoft.alexa.skills.audiotennis.enums;

public enum ActivityType {

    ALPHABET_RACE(true),

    LAST_LETTER(true),

    BAM_WHAM(false),

    RHYME_MATCH(false);

    private final boolean competition;

    ActivityType(boolean competition) {
        this.competition = competition;
    }

    public boolean isCompetition() {
        return competition;
    }
}
