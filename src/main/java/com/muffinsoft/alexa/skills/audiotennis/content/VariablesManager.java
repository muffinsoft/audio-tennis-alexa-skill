package com.muffinsoft.alexa.skills.audiotennis.content;

import com.muffinsoft.alexa.sdk.content.BaseContentManager;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;

public class VariablesManager extends BaseContentManager<PhraseContainer> {

    public VariablesManager(String path) {
        super(path);
    }

    @Override
    public PhraseContainer getValueByKey(String key) {

        Object valueByKey = super.getValueByKey(key);

        if (valueByKey == null) {
            throw new IllegalArgumentException("Can't find text by key: " + key);
        }

        return getObjectMapper().convertValue(valueByKey, BasePhraseContainer.class);
    }
}
