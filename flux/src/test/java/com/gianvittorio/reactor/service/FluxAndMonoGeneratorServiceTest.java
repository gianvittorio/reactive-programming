package com.gianvittorio.reactor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    private FluxAndMonoGeneratorService generatorService;

    @BeforeEach
    public void setUp() {
        generatorService = new FluxAndMonoGeneratorService();
    }

    @Test
    @DisplayName("Must subscribe and consume the following list of names.")
    public void namesFluxTest() {
        StepVerifier.create(generatorService.namesFlux())
                .expectSubscription()
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must susbscribe and consume single name.")
    public void namesMonoTest() {
        StepVerifier.create(generatorService.namesMono())
                .expectSubscription()
                .expectNext("alex")
                .verifyComplete();
    }
}
