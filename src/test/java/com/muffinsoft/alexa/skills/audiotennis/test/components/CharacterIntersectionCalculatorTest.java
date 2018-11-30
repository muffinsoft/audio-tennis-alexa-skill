package com.muffinsoft.alexa.skills.audiotennis.test.components;

import com.muffinsoft.alexa.skills.audiotennis.components.CharacterIntersectionCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharacterIntersectionCalculatorTest {

    @Test
    void goldenPath() {

        float result = CharacterIntersectionCalculator.calculate("help", "elp");

        Assertions.assertEquals(result, 75);
    }
}
