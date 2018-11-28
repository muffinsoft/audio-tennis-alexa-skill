package com.muffinsoft.alexa.skills.audiotennis.activities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Slot;
import com.muffinsoft.alexa.sdk.activities.BaseStateManager;
import com.muffinsoft.alexa.sdk.model.DialogItem;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;
import com.muffinsoft.alexa.skills.audiotennis.components.UserReplyComparator;
import com.muffinsoft.alexa.skills.audiotennis.content.AliasManager;
import com.muffinsoft.alexa.skills.audiotennis.content.RegularPhraseManager;
import com.muffinsoft.alexa.skills.audiotennis.enums.UserReplies;
import com.muffinsoft.alexa.skills.audiotennis.models.PhraseDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.SettingsDependencyContainer;
import com.muffinsoft.alexa.skills.audiotennis.models.UserProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.muffinsoft.alexa.skills.audiotennis.constants.PhraseConstants.SELECT_MISSION_PHRASE;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.CURRENT_MISSION;
import static com.muffinsoft.alexa.skills.audiotennis.constants.SessionConstants.USER_PROGRESS;

public class SelectLevelStateManager extends BaseStateManager {

    private static final Logger logger = LogManager.getLogger(SelectLevelStateManager.class);

    private final AliasManager aliasManager;
    private final RegularPhraseManager regularPhraseManager;
    private UserProgress userProgress;

    public SelectLevelStateManager(Map<String, Slot> slots, AttributesManager attributesManager, SettingsDependencyContainer settingsDependencyContainer, PhraseDependencyContainer phraseDependencyContainer) {
        super(slots, attributesManager, settingsDependencyContainer.getDialogTranslator());
        this.aliasManager = settingsDependencyContainer.getAliasManager();
        this.regularPhraseManager = phraseDependencyContainer.getRegularPhraseManager();
    }

    @Override
    protected void populateActivityVariables() {
        LinkedHashMap rawUserProgress = (LinkedHashMap) getSessionAttributes().get(USER_PROGRESS);
        userProgress = rawUserProgress != null ? mapper.convertValue(rawUserProgress, UserProgress.class) : new UserProgress();
    }

    @Override
    public DialogItem nextResponse() {

        logger.debug("Starting handling user reply '" + this.getUserReply() + "' ...");

        List<PhraseContainer> dialog = null;
        if (UserReplyComparator.compare(getUserReply(), UserReplies.YES)) {
            dialog = regularPhraseManager.getValueByKey(SELECT_MISSION_PHRASE);
        }

        String cardTitle = null;
        if (this.getSessionAttributes().containsKey(CURRENT_MISSION)) {
            cardTitle = aliasManager.getValueByKey(String.valueOf(this.getSessionAttributes().get(CURRENT_MISSION)));
        }

        DialogItem.Builder builder = DialogItem.builder().addResponse(getDialogTranslator().translate(dialog));

        if (cardTitle != null) {
            builder.withCardTitle(cardTitle);
        }

        return builder.build();
    }
}
