package com.muffinsoft.alexa.skills.audiotennis.components;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class ObjectConvert {

    public static LinkedHashMap toMap(Object progress) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(progress, LinkedHashMap.class);
    }
}
