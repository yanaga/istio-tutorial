package com.redhat.developer.demos.customer;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private static final String RESPONSE_STRING_FORMAT = "customer => %s\n";

    private final PreferenceRepository preferenceRepository;

    public CustomerController(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @RequestMapping("/")
    public String getCustomer(@RequestHeader("User-Agent") String userAgent) {
        return String.format(RESPONSE_STRING_FORMAT, preferenceRepository.get());
    }

}
