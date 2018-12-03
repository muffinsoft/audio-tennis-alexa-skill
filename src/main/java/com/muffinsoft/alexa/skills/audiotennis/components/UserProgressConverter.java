package com.muffinsoft.alexa.skills.audiotennis.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.io.IOException;
import java.util.LinkedHashMap;

public class UserProgressConverter {

    public static UserProgress fromJson(String json) {
        if (json != null && !json.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                LinkedHashMap rawUserProgress = mapper.readValue(json, LinkedHashMap.class);
                return mapper.convertValue(rawUserProgress, UserProgress.class);
            }
            catch (IOException e) {
                return new UserProgress();
            }
        }
        else {
            return new UserProgress();
        }
    }

    public static String toJson(UserProgress userProgress) {
        if (userProgress == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(userProgress);
        }
        catch (JsonProcessingException e) {
            return null;
        }
    }
}
