package com.redhat.developer.demos.customer;

import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CustomerCamelRoute extends RouteBuilder {

    private static final String RESPONSE_STRING_FORMAT = "customer => %s\n";

    @Override
    public void configure() throws Exception {
        from("direct:customer")
                .routeId("customer-route")
                .onException(ExchangeTimedOutException.class)
                    .log(LoggingLevel.WARN, "Timeout waiting for reply on message with id ${id}")
                    .continued(true)
                    .transform(simple("Timeout waiting for reply on message ${id}"))
                .end()
                .inOut("jms:preferenceQueue?requestTimeout=500&timeToLive=500")
                .process(exchange -> {
                    String input = exchange.getIn().getBody(String.class);
                    exchange.getOut().setBody(String.format(RESPONSE_STRING_FORMAT, input));
                });
    }

}
