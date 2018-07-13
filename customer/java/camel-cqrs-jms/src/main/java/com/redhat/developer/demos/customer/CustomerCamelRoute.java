package com.redhat.developer.demos.customer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CustomerCamelRoute extends RouteBuilder {

    private final PreferenceRepository preferenceRepository;

    public CustomerCamelRoute(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public void configure() throws Exception {
        from("jms:preferenceTopic")
                .routeId("customer-route")
                .log(LoggingLevel.INFO, "Received preference message ${id}")
                .process(e -> preferenceRepository.save(e.getIn().getBody(String.class)));
    }

}
