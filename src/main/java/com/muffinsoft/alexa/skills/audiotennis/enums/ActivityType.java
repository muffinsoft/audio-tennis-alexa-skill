package com.muffinsoft.alexa.skills.audiotennis.enums;

import java.util.Arrays;
import java.util.List;

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

    private static final ActivityType[] paidArray = {RHYME_MATCH, BAM_WHAM};
    public static final List<ActivityType> PAID = Arrays.asList(paidArray);
}
