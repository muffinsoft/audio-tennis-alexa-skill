package com.muffinsoft.alexa.skills.audiotennis.components;

import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.model.SlotName;
import com.muffinsoft.alexa.sdk.util.SlotComputer;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;

import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.ALPHABET_RACE;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.BAM_WHAM;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.LAST_LETTER;
import static com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType.RHYME_MATCH;

public class ActivityPuller {

    public static ActivityType getActivityFromReply(Map<String, Slot> inputSlots) {
        List<String> userReplies = SlotComputer.compute(inputSlots).get(SlotName.MISSION);
        userReplies.addAll(SlotComputer.compute(inputSlots).get(SlotName.ACTION));
        for (String reply : userReplies) {
            if (UserReplyComparator.compare(reply, UserReplies.LAST_LETTER)) {
                return LAST_LETTER;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.BAM_WHAM)) {
                return BAM_WHAM;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.ALPHABET_RACE)) {
                return ALPHABET_RACE;
            }
            else if (UserReplyComparator.compare(reply, UserReplies.RHYME_MATCH)) {
                return RHYME_MATCH;
            }
        }
        return null;
    }
}
