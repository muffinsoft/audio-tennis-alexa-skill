package com.muffinsoft.alexa.skills.audiotennis.test;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.exception.PersistenceException;
import com.amazon.ask.model.RequestEnvelope;

import java.util.Map;
import java.util.Optional;

public class MockPersistenceAdapter implements PersistenceAdapter {

    @Override
    public Optional<Map<String, Object>> getAttributes(RequestEnvelope envelope) throws PersistenceException {
        return Optional.empty();
    }

    @Override
    public void saveAttributes(RequestEnvelope envelope, Map<String, Object> attributes) throws PersistenceException {

    }

    @Override
    public void deleteAttributes(RequestEnvelope envelope) throws PersistenceException {

    }
}
