package com.gianvittorio.reactor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
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

    @Test
    public void exploreParallelTest() {
        // Given

        // When
        ParallelFlux<String> flux = fluxAndMonoSchedulerService.exploreParallel();

        // Then
        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNextCount(3)
                .expectComplete();
    }

    @Test
    public void exploreParallelUsingFlatMapTest() {
        // Given

        // When
        Flux<String> flux = fluxAndMonoSchedulerService.exploreParallelUsingFlatMap();

        // Then
        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNextCount(3)
                .expectComplete();
    }

    @Test
    public void exploreParallelUsingFlatMapSequentialTest() {
        // Given

        // When
        Flux<String> flux = fluxAndMonoSchedulerService.exploreParallelUsingFlatMapSequential();

        // Then
        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("ALEX", "BEN", "CHLOE")
                .expectComplete();
    }

    @Test
    public void exploreParallelUsingFlatMap1Test() {
        // Given

        // When
        Flux<String> flux = fluxAndMonoSchedulerService.exploreParallelUsingFlatMap1();

        // Then
        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNextCount(6)
                .expectComplete();
    }
}
