package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.activities.StateManager;
import com.muffinsoft.alexa.sdk.components.DialogTranslator;
import com.muffinsoft.alexa.sdk.constants.PaywallConstants;
import com.muffinsoft.alexa.sdk.handlers.BuyIntentHandler;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.skills.audiotennis.components.BuyManager;
import com.muffinsoft.alexa.skills.audiotennis.components.UserProgressConverter;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;

import java.util.LinkedHashMap;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.ACTIVITY_PROGRESS;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;

public class TennisBuyIntentHandler extends BuyIntentHandler {

    private final DialogTranslator dialogTranslator;
    private final PhraseDependencyContainer phraseDependencyContainer;

    public TennisBuyIntentHandler(DialogTranslator dialogTranslator, PhraseDependencyContainer phraseDependencyContainer) {
        this.dialogTranslator = dialogTranslator;
        this.phraseDependencyContainer = phraseDependencyContainer;
    }

    @Override
    public StateManager nextTurn(HandlerInput input) {
        return new BaseStateManager(getSlotsFromInput(input), input.getAttributesManager(), dialogTranslator) {
            @Override
            public DialogItem nextResponse() {

                UserProgress userProgress = UserProgressConverter.fromJson(String.valueOf(getPersistentAttributes().get(USER_PROGRESS)));

                LinkedHashMap rawActivityProgress = (LinkedHashMap) getSessionAttributes().get(ACTIVITY_PROGRESS);
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
                            this.getPersistentAttributes().put(SessionConstants.USER_PROGRESS, json);
                        }
                        savePersistentAttributes();
                    }
                }
                return BuyManager.getBuyResponse(input.getAttributesManager(), phraseDependencyContainer, dialogTranslator, PaywallConstants.BUY);
            }
        };
    }
}
