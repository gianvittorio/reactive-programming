package com.gianvittorio.reactor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    private FluxAndMonoGeneratorService generatorService;

    @BeforeEach
    public void setUp() {
        generatorService = new FluxAndMonoGeneratorService();
    }

    @Test
    @DisplayName("Must subscribe and consume the following list of names.")
    public void namesFluxTest() {
        StepVerifier.create(generatorService.namesFlux().log())
                .expectSubscription()
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must susbscribe and consume single name.")
    public void namesMonoTest() {
        StepVerifier.create(generatorService.namesMono().log())
                .expectSubscription()
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return uppercase only names list.")
    public void namesFluxMapTest() {
        StepVerifier.create(generatorService.namesFluxMap().log())
                .expectSubscription()
                .expectNext("4 - ALEX", "3 - BEN", "5 - CHLOE")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return transformed flux.")
    public void namesFluxFlatMapTest() {
        final int length = 3;

        StepVerifier.create(generatorService.namesFluxFlatMap(length).log())
                .expectSubscription()
                .expectNext("A", "L", "E", "X")
                .expectNext("C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return transformed flux.")
    public void namesFluxTransformTest() {
        final int length = 3;

        StepVerifier.create(generatorService.namesFluxTransform(length).log())
                .expectSubscription()
                .expectNext("A", "L", "E", "X")
                .expectNext("C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return transformed flux.")
    public void namesFluxTransform1Test() {
        final int length = 6;

        StepVerifier.create(generatorService.namesFluxTransform(length).log())
                .expectSubscription()
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return transformed flux.")
    public void namesFluxTransformSwitchIfEmptyTest() {
        final int length = 6;

        StepVerifier.create(generatorService.namesFluxTransformSwitchIfEmpty(length).log())
                .expectSubscription()
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return transformed flux.")
    public void namesFluxFlatMapAsyncTest() {
        final int length = 3;

        StepVerifier.create(generatorService.namesFluxFlatMapAsync(length).log())
                .expectSubscription()
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return transformed flux.")
    public void namesFluxConcatMapTest() {
        final int length = 3;

        StepVerifier.create(generatorService.namesFluxConcatMap(length).log())
                .expectSubscription()
                .expectNext("A", "L", "E", "X")
                .expectNext("C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return uppercase only name.")
    public void nameMonoMapFilterTest() {
        StepVerifier.create(generatorService.nameMonoMapFilter(3).log())
                .expectSubscription()
                .expectNext("4 - ALEX")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return uppercase only name.")
    public void nameMonoMapFilterDefaultIfEmptyTest() {
        StepVerifier.create(generatorService.nameMonoMapFilter(4).log())
                .expectSubscription()
                .expectNext("7 - DEFAULT")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return uppercase only name.")
    public void nameMonoMapFilterSwitchIfEmptyTest() {
        StepVerifier.create(generatorService.nameMonoMapFilterSwitchIfEmpty(4).log())
                .expectSubscription()
                .expectNext("7 - DEFAULT")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return uppercase only name.")
    public void nameMonoFlatMapTest() {
        StepVerifier.create(generatorService.nameMonoFlatMap(3).log())
                .expectSubscription()
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return uppercase only name.")
    public void nameMonoFlatMapManyTest() {
        StepVerifier.create(generatorService.nameMonoFlatMapMany(3).log())
                .expectSubscription()
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must assert immutability.")
    public void namesFluxImmutabilityTest() {
        StepVerifier.create(generatorService.namesFluxImmutable().log())
                .expectSubscription()
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must only return names which length is greater than 3.")
    public void namesFluxGreaterThan3Test() {
        final int length = 3;

        StepVerifier.create(generatorService.nameFluxFilterGreaterThan(length).log())
                .expectSubscription()
                .expectNext("alex", "chloe")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must only return name which length is greater than 3.")
    public void nameMonoGreaterThan3Test() {
        final int length = 3;

        StepVerifier.create(generatorService.nameMonoFilterGreaterThan(length).log())
                .expectSubscription()
                .expectNext("alex")
                .verifyComplete();
    }
}
