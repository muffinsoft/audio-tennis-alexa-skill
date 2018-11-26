package com.muffinsoft.alexa.skills.audiotennis.content;


import com.muffinsoft.alexa.sdk.content.BaseContentManager;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.ArrayList;
import java.util.List;

public class GreetingsManager extends BaseContentManager<List<BasePhraseContainer>> {

    public GreetingsManager(String path) {
        super(path);
    }

    @Override
    public List<BasePhraseContainer> getValueByKey(String key) {

        List valueByKey = super.getValueByKey(key);

        List<BasePhraseContainer> resultList = new ArrayList<>(valueByKey.size() * 2);

        for (Object raw : valueByKey) {
            resultList.add(getObjectMapper().convertValue(raw, BasePhraseContainer.class));
        }
        return resultList;
    }
}
