package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.skills.audiotennis.constants.GreetingsConstants;
import com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants;
import com.muffinsoft.alexa.skills.audiotennis.content.GreetingsManager;
import com.muffinsoft.alexa.skills.audiotennis.content.PhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ActivityProgress;
import com.muffinsoft.alexa.skills.audiotennis.models.ConfigContainer;

import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.sdk.constants.SessionConstants.INTENT;
import static com.muffinsoft.alexa.sdk.constants.SessionConstants.USER_REPLY_BREAKPOINT;
import static com.muffinsoft.alexa.sdk.enums.IntentType.GAME;
import static com.muffinsoft.alexa.sdk.enums.IntentType.INITIAL_GREETING;

public class InitialGreetingStateManager extends BaseStateManager {

    private final GreetingsManager greetingsManager;
    private final PhraseManager phraseManager;

    private Integer userReplyBreakpointPosition;

    public InitialGreetingStateManager(Map<String, Slot> inputSlots, AttributesManager attributesManager, ConfigContainer configContainer) {
        super(inputSlots, attributesManager, configContainer.getDialogTranslator());
        this.greetingsManager = configContainer.getGreetingsManager();
        this.phraseManager = configContainer.getPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        this.userReplyBreakpointPosition = (Integer) this.getSessionAttributes().getOrDefault(USER_REPLY_BREAKPOINT, null);
    }

    @Override
    public DialogItem nextResponse() {

        DialogItem.Builder builder = DialogItem.builder();

        List<BasePhraseContainer> dialog = greetingsManager.getValueByKey(GreetingsConstants.FIRST_TIME_GREETING);

        this.getSessionAttributes().remove(USER_REPLY_BREAKPOINT);
        this.getSessionAttributes().put(INTENT, GAME);

        int index = 0;
        for (PhraseContainer phraseSettings : dialog) {

            index++;

            if (this.userReplyBreakpointPosition != null && index <= this.userReplyBreakpointPosition) {
                continue;
            }

            if (phraseSettings.isUserResponse()) {
                this.getSessionAttributes().put(SessionConstants.USER_REPLY_BREAKPOINT, index + 1);
                this.getSessionAttributes().put(INTENT, INITIAL_GREETING);
                break;
            }
            builder.addResponse(getDialogTranslator().translate(phraseSettings));
        }

        if (index >= dialog.size()) {
            addFirstActivityIntro(builder);
        }

        return builder
                .withSlotName(SlotName.ACTION.text)
                .turnOffReprompt()
                .build();
    }

    private void addFirstActivityIntro(DialogItem.Builder builder) {
        ActivityType defaultActivity = ActivityProgress.getDefaultActivity();
        BasePhraseContainer container = new BasePhraseContainer();
        builder.addResponse(getDialogTranslator().translate((container)));
    }
}
