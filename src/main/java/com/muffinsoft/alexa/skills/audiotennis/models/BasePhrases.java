package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

abstract class BasePhrases {

    @JsonIgnore
    int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }

    @JsonIgnore
    List<BasePhraseContainer> removeAllPartialElements(List<BasePhraseContainer> initialList) {
        List<BasePhraseContainer> resultList = new ArrayList<>();
        for (BasePhraseContainer container : initialList) {
            if (container.getRole().equals("Audio")) {
                String link = container.getAudio();
                if (!link.contains("_1") && !link.contains("_2") && !link.contains("_3")) {
                    resultList.add(container);
                }
            }
            else {
                resultList.add(container);
            }
        }
        return resultList;
    }
}
