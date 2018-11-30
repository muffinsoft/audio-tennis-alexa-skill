package com.muffinsoft.alexa.skills.audiotennis.components;

public class CharacterIntersectionCalculator {

    public static float calculate(String first, String second) {
        float weightOfOneLetter = 100F / first.length();

        float totalWeight = 100f;

        for (char c : first.toCharArray()) {
            if (!second.contains(String.valueOf(c))) {
                totalWeight -= weightOfOneLetter;
            }
        }

        return totalWeight;
    }
}
