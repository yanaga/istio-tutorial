package com.redhat.developer.demos.preference;

public interface RecommendationRepository {

    public void save(String preference);

    public String get();

}
