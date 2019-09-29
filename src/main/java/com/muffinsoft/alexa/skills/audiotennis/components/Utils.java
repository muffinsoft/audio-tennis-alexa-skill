package com.muffinsoft.alexa.skills.audiotennis.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.constants.PaywallConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

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

                if (sessionAttributes.containsKey(ActivityType.ALPHABET_RACE.name())) {
                    userProgress.getAlreadyPlayed().add(ActivityType.ALPHABET_RACE.name());
                }
                if (sessionAttributes.containsKey(ActivityType.RHYME_MATCH.name())) {
                    userProgress.getAlreadyPlayed().add(ActivityType.RHYME_MATCH.name());
                }

                String json = UserProgressConverter.toJson(userProgress);
                if (json != null) {
                    persistentAttributes.put(SessionConstants.USER_PROGRESS, json);
                }
            }
        }
    }

    public static void restoreAlreadyPlayed(Map<String, Object> persistent, Map<String, Object> session) {
        Object lastUpSell = persistent.get(PaywallConstants.UPSELL);
        if (lastUpSell != null) {
            ZonedDateTime lastUpSellTime = Instant.parse(lastUpSell.toString()).atZone(ZoneId.systemDefault());
            UserProgress userProgress = UserProgressConverter.fromJson(String.valueOf(persistent.get(USER_PROGRESS)));
            if (lastUpSellTime.plusMinutes(20).isAfter(ZonedDateTime.now())) {
                logger.debug("Restoring already played: {}", userProgress.getAlreadyPlayed().size());
                userProgress.getAlreadyPlayed().forEach(o -> session.put(o, "true"));
            }
            userProgress.getAlreadyPlayed().clear();
            logger.debug("Clearing already played");
        }
    }
}
