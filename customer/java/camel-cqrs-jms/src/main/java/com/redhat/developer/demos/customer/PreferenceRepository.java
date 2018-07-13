package com.redhat.developer.demos.customer;

public interface PreferenceRepository {

    public void save(String preference);

    public String get();

}
