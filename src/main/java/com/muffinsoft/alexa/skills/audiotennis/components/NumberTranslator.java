package com.muffinsoft.alexa.skills.audiotennis.components;

public class NumberTranslator {

    public static String translateToOrdinalValue(int number) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (number % 100) {
            case 11:
            case 12:
            case 13:
                return number + "th";
            default:
                return number + suffixes[number % 10];
        }
    }
}
