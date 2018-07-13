package com.redhat.developer.demos.customer;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class InMemoryPreferenceRepository implements PreferenceRepository {

    private final AtomicReference<String> value = new AtomicReference<>("default cached preference");

    @Override
    public void save(String preference) {
        value.set(preference);
    }

    @Override
    public String get() {
        return value.get();
    }

}
