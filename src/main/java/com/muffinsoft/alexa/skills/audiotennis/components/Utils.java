package com.muffinsoft.alexa.skills.audiotennis.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;

public class Utils {

    public static void saveProgressBeforeUpsell(Map<String, Object> persistentAttributes, Map<String, Object> sessionAttributes, ObjectMapper mapper) {
        UserProgress userProgress = UserProgressConverter.fromJson(String.valueOf(persistentAttributes.get(USER_PROGRESS)));

        LinkedHashMap rawActivityProgress = (LinkedHashMap) sessionAttributes.get(ACTIVITY_PROGRESS);
        ActivityProgress activityProgress = rawActivityProgress != null ? mapper.convertValue(rawActivityProgress, ActivityProgress.class) : null;
        if (activityProgress != null) {
            activityProgress.getUnlockedActivities().remove(null);
            if (userProgress != null) {
                userProgress.setLastGameEnemyPoint(activityProgress.getEnemyPointCounter());
                userProgress.setLastGamePlayerPoint(activityProgress.getPlayerPointCounter());
                userProgress.setMoveToUpSell(true);
                userProgress.setPlayerPointsBeforeUpSell(activityProgress.getPlayerPointCounter());
                userProgress.setPlayerScoresBeforeUpSell(activityProgress.getPlayerGameCounter());
                userProgress.setPlayerPointWinInRow(activityProgress.getPlayerPointWinInRow());
                userProgress.setEnemyPointWinInRow(activityProgress.getEnemyPointWinInRow());
                userProgress.setEnemyPointsBeforeUpSell(activityProgress.getEnemyPointCounter());
                userProgress.setEnemyScoresBeforeUpSell(activityProgress.getEnemyGameCounter());

                String json = UserProgressConverter.toJson(userProgress);
                if (json != null) {
                    persistentAttributes.put(SessionConstants.USER_PROGRESS, json);
                }
            }
        }
    }
}
