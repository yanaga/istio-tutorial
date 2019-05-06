package com.redhat.developer.demos.recommendation.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class RecommendationResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    @ConfigProperty(name = "version", defaultValue = "v1")
    Version version;

    @Inject
    @ConfigProperty(name = "hostname", defaultValue = "unknown")
    Hostname hostname;

    String responseStringFormat;

    @PostConstruct
    void init() {
        this.responseStringFormat = String.format("recommendation %s from '%s': %%s", version, hostname);
    }

    @Inject
    @RestClient
    WorldTimeService worldTimeService;

    /**
     * Counter to help us see the lifecycle
     */
    private int count = 1;

    /**
     * Flag for throwing a 503 when enabled
     */
    private boolean misbehave = false;

    @GET
    public Response getRecommendations() {
        logger.info(String.format("recommendation request from %s: %d", hostname, count));

        if (version.isTimeout()) {
            timeout();
        }

        logger.debug("recommendation service ready to return");

        if (misbehave) {
            return doMisbehavior();
        }
        if (version.isMakeExternalRequest()) {
            try {
                return Response.ok(String.format(responseStringFormat, worldTimeService.getNow())).build();
            } catch (WebApplicationException ex) {
                return Response.ok(String.format(responseStringFormat, ex.getMessage())).build();
            }
        } else {
            return Response.ok(String.format(responseStringFormat, count++)).build();
        }
    }

    private void timeout() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.info("Thread interrupted");
        }
    }

    private Response doMisbehavior() {
        logger.debug(String.format("Misbehaving %d", count));
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(String.format("recommendation misbehavior from '%s'\n", hostname)).build();
    }

    @GET
    @Path("/misbehave")
    public Response flagMisbehave() {
        this.misbehave = true;
        logger.debug("'misbehave' has been set to 'true'");
        return Response.ok("Following requests to / will return 503\n").build();
    }

    @GET
    @Path("/behave")
    public Response flagBehave() {
        this.misbehave = false;
        logger.debug("'misbehave' has been set to 'false'");
        return Response.ok("Following requests to / will return 200\n").build();
    }

}
