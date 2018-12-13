package com.muffinsoft.alexa.skills.audiotennis.handlers;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.components.IntentFactory;
import com.muffinsoft.alexa.sdk.model.SlotName;

import java.util.Map;

public class TennisYesIntentHandler extends TennisGameIntentHandler {

    public TennisYesIntentHandler(IntentFactory intentFactory) {
        super(intentFactory);
    }

    @Override
    protected String getIntentName() {
        return "AMAZON.YesIntent";
    }

    @Override
    protected boolean shouldAppendSlotValues() {
        return true;
    }

    @Override
    protected void appendValuesToSlots(Map<String, Slot> slots) {
        Slot.Builder slotBuilder = Slot.builder().withValue("yes");
        slots.put(SlotName.CONFIRMATION.text, slotBuilder.build());
    }
}
