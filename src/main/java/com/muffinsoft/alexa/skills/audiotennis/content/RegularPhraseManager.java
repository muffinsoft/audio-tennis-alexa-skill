package com.muffinsoft.alexa.skills.audiotennis.content;

import com.muffinsoft.alexa.sdk.content.BaseContentManager;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;
import com.muffinsoft.alexa.sdk.model.PhraseContainer;

import java.util.ArrayList;
import java.util.List;

public class RegularPhraseManager extends BaseContentManager<List<PhraseContainer>> {

    public RegularPhraseManager(String path) {
        super(path);
    }

    @Override
    public List<PhraseContainer> getValueByKey(String key) {

        List valueByKey = super.getValueByKey(key);

        List<PhraseContainer> resultList = new ArrayList<>(valueByKey.size() * 2);

        for (Object raw : valueByKey) {
            resultList.add(getObjectMapper().convertValue(raw, BasePhraseContainer.class));
        }
        return resultList;
    }
}