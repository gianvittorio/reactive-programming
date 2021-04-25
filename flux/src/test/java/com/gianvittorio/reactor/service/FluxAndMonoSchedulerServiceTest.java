package com.gianvittorio.reactor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoSchedulerServiceTest {

    private FluxAndMonoSchedulerService fluxAndMonoSchedulerService;

    @BeforeEach
    public void setUp() {
        fluxAndMonoSchedulerService = new FluxAndMonoSchedulerService();
    }

    @Test
    public void explorePublishOnTest() {
        // Given

        // When
        Flux<String> flux = fluxAndMonoSchedulerService.explorePublishOn();

        // Then
        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void exploreSubscribeOnTest() {
        // Given

        // When
        Flux<String> flux = fluxAndMonoSchedulerService.exploreSubscribeOn();

        // Then
        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }
}
