package com.redhat.developer.demos.preference;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PreferenceCamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:preferenceQueue")
                .routeId("preference-route")
                .log(LoggingLevel.INFO, "Received preference message ${id}")
                .onException(ExchangeTimedOutException.class)
                    .log(LoggingLevel.WARN, "Timeout waiting for reply on message ${id}")
                    .continued(true)
                    .transform(simple("Timeout waiting for reply on message ${id}"))
                .end()
                .inOut("jms:recommendationQueue?requestTimeout=250&timeToLive=250")
                .transform(body().prepend("preference => "));
    }

}