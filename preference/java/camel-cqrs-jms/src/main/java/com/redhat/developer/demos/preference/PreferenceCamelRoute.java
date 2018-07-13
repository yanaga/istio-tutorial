package com.redhat.developer.demos.preference;

import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PreferenceCamelRoute extends RouteBuilder {

    private final RecommendationRepository recommendationRepository;

    public PreferenceCamelRoute(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public void configure() throws Exception {
        from("jms:recommendationTopic")
                .routeId("preference-route")
                .log(LoggingLevel.INFO, "Received preference message ${id}")
                .process(e -> recommendationRepository.save(e.getIn().getBody(String.class)))
                .transform(body().prepend("preference => "))
                .to("jms:preferenceTopic");
        from("timer://simple?period=10s&delay=10s")
                .routeId("preferenceTimer-route")
                .log(LoggingLevel.INFO, "Sending timed preference message ${id}")
                .process(e -> e.getOut().setBody(recommendationRepository.get()))
                .transform(body().prepend("preference => "))
                .to("jms:preferenceTopic");
    }

}