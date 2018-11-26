package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.enums.IntentType;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.constants.GreetingsConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.CardManager;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_PROGRESS;
import static com.muffinsoft.alexa.skills.audiotennis.constants.CardConstants.WELCOME_CARD;

public class LaunchStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(LaunchStateManager.class);
    private final RegularPhraseManager regularPhraseManager;
    private final GreetingsManager greetingsManager;
    private final CardManager cardManager;

    private UserProgress userProgress;

    public LaunchStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer.getDialogTranslator());
        this.regularPhraseManager = configContainer.getRegularPhraseManager();
        this.cardManager = configContainer.getCardManager();
        this.greetingsManager = configContainer.getGreetingsManager();
    }

    @Override
    protected void populateActivityVariables() {
        LinkedHashMap rawUserProgress = (LinkedHashMap) getSessionAttributes().get(USER_PROGRESS);
        this.userProgress = rawUserProgress != null ? mapper.convertValue(rawUserProgress, UserProgress.class) : null;
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        if (userProgress != null) {
            buildRoyalGreeting(builder);

            logger.info("Existing user was started new Game Session. Start Royal Greeting");
        }
        else {
            buildInitialGreeting(builder);

            getSessionAttributes().put(INTENT, IntentType.INITIAL_GREETING);

            logger.info("New user was started new Game Session.");
        }

        return builder
                .withCardTitle(cardManager.getValueByKey(WELCOME_CARD))
                .build();
    }

    private void buildInitialGreeting(DialogItem.Builder builder) {
        List<BasePhraseContainer> dialog = greetingsManager.getValueByKey(GreetingsConstants.FIRST_TIME_GREETING);

        int userReplyBreakpointPosition = 0;

        for (PhraseContainer phraseSettings : dialog) {

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, userReplyBreakpointPosition + 1);
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
            userReplyBreakpointPosition++;
        }
    }

    private void buildRoyalGreeting(DialogItem.Builder builder) {
        buildInitialGreeting(builder);
    }
}
