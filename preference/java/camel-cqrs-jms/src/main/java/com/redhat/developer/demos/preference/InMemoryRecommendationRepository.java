package com.redhat.developer.demos.preference;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class InMemoryRecommendationRepository implements RecommendationRepository {

    private final AtomicReference<String> value = new AtomicReference<>("default cached recommendation");

    @Override
    public void save(String preference) {
        value.set(preference);
    }

    @Override
    public String get() {
        return value.get();
    }

}
