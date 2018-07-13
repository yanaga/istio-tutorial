package com.redhat.developer.demos.recommendation;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RecommendationCamelRoute extends RouteBuilder {

    private static final String RESPONSE_STRING_FORMAT = "recommendation v1 from '%s': %d";

    private static final String HOSTNAME = parseContainerIdFromHostname(
            System.getenv().getOrDefault("HOSTNAME", "unknown")
    );

    private int count = 0;

    static String parseContainerIdFromHostname(String hostname) {
        return hostname.replaceAll("recommendation-v\\d+-", "");
    }

    @Override
    public void configure() throws Exception {
        from("jms:recommendationQueue")
                .routeId("recommendation-route")
                .log(LoggingLevel.INFO, String.format("recommendation request from %s: ${id}", HOSTNAME))
                .process(exchange -> exchange.getOut().setBody(
                        String.format(RESPONSE_STRING_FORMAT, HOSTNAME, count++)
                ));
    }

}