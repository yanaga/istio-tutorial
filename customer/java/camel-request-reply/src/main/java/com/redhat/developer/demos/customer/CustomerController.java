package com.redhat.developer.demos.customer;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CamelContext camelContext;

    public CustomerController(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @RequestMapping("/")
    public String getCustomer(@RequestHeader("User-Agent") String userAgent) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        return template.requestBody("direct:customer", userAgent, String.class);
    }

}
