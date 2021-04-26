package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.exception.ReactorException;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
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
    @DisplayName("Must return transformed flux.")
    public void namesFluxConcatMapVirtualTimerTest() {
        final int length = 3;

        VirtualTimeScheduler.getOrSet();

        StepVerifier.withVirtualTime(() -> generatorService.namesFluxConcatMap(length).log())
                .thenAwait(Duration.ofSeconds(10))
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

    @Test
    @DisplayName("Must concatenate 2 flux.")
    public void fluxConcatTest() {

        Flux<String> concatFlux = generatorService.exploreConcat();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("D", "E", "F")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must concatenate 2 flux.")
    public void fluxConcatWithTest() {

        Flux<String> concatFlux = generatorService.exploreConcatWith();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("D", "E", "F")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must concatenate 2 mono.")
    public void fluxConcatTestWithMono() {

        Flux<String> concatFlux = generatorService.exploreConcatWithMono();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must merge 2 flux.")
    public void fluxMergeTest() {

        Flux<String> concatFlux = generatorService.exploreMerge();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must merge 2 flux.")
    public void fluxMergeWithTest() {

        Flux<String> concatFlux = generatorService.exploreMergeWith();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must merge 2 flux sequantially.")
    public void fluxMergeSequentialTest() {

        Flux<String> concatFlux = generatorService.exploreMergeSequential();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must merge 2 monos.")
    public void fluxMergeWithMonoTest() {

        Flux<String> concatFlux = generatorService.exploreMergeWithMono();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must zip 2 flux.")
    public void fluxZipTest() {

        Flux<String> concatFlux = generatorService.exploreZip();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must zip 2 flux.")
    public void fluxZipWithTest() {

        Flux<String> concatFlux = generatorService.exploreZipWith();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must zip 2 monos.")
    public void monoZipWithTest() {

        Mono<String> concatFlux = generatorService.exploreZipWithMono();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must zip 4 flux.")
    public void fluxZip1Test() {

        Flux<String> concatFlux = generatorService.exploreZip1();

        StepVerifier.create(concatFlux.log())
                .expectSubscription()
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    @DisplayName("Return error, then cancel subscription.")
    public void fluxExceptionTest() {
        StepVerifier.create(generatorService.exceptionFlux().log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    @DisplayName("Return error, then cancel subscription.")
    public void fluxException1Test() {
        StepVerifier.create(generatorService.exceptionFlux().log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("Return error, then cancel subscription.")
    public void fluxException2Test() {
        StepVerifier.create(generatorService.exceptionFlux().log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectErrorMessage("Exception occurred")
                .verify();
    }

    @Test
    @DisplayName("Must return error flux.")
    public void fluxOnErrorResumeTest() {
        // Given
        Exception exception = new IllegalStateException("Invalid state");

        // When
        Flux<String> value = generatorService.exploreOnErrorResume(exception);

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("D", "E", "F")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return error flux.")
    public void fluxOnErrorResume1Test() {
        // Given
        Exception exception = new RuntimeException("Invalid state");

        // When
        Flux<String> value = generatorService.exploreOnErrorResume(exception);

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Must return error flux.")
    public void fluxOnErrorContinueTest() {
        // Given
        Exception exception = new IllegalStateException("Invalid state");

        // When
        Flux<String> value = generatorService.exploreOnErrorContinue(exception);

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext("A", "C", "D")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return error flux.")
    public void fluxOnErrorMapTest() {
        // Given

        // When
        Flux<String> value = generatorService.exploreOnErrorMap();

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    @DisplayName("Must return error flux.")
    public void fluxOnErrorTest() {
        // Given

        // When
        Flux<String> value = generatorService.exploreDoOnError();

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("Must return error mono.")
    public void fluxOnErrorMonoTest() {
        // Given

        // When
        var value = generatorService.exploreDoOnErrorMono();

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext("ABC")
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return identity mono.")
    public void fluxOnErrorContinueMonoWhenInputIsValidTest() {
        // Given

        // When
        final String expectedName = "reactor";
        Mono<String> value = generatorService.exploreOnErrorContinueMono(expectedName);

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectNext(expectedName)
                .expectComplete();
    }

    @Test
    @DisplayName("Must return error mono.")
    public void fluxOnErrorContinueMonoWhenInputIsInvalidTest() {
        // Given

        // When
        Mono<String> value = generatorService.exploreOnErrorContinueMono("abc");

        // Then
        StepVerifier.create(value.log())
                .expectSubscription()
                .expectComplete();
    }

    @Test
    @DisplayName("Must generate [1,10] and multiply each item by 2.")
    public void exploreGenerateTest() {
        // Given

        // When
        Flux<Integer> integerFlux = generatorService.exploreGenerate();

        // Then
        StepVerifier.create(integerFlux.log())
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must create sequence as by wrapped up factory method.")
    public void exploreCreateTest() {
        // Given

        // When
        Flux<String> flux = generatorService.exploreCreate();

        // Then
        StepVerifier.create(flux.log())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must create single item.")
    public void exploreCreateMonoTest() {
        // Given

        // When
        Mono<String> mono = generatorService.exploreCreateMono();

        // Then
        StepVerifier.create(mono.log())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must handle sequence as by wrapped up factory method.")
    public void exploreHandleTest() {
        // Given

        // When
        Flux<String> flux = generatorService.exploreHandle();

        // Then
        StepVerifier.create(flux.log())
                .expectNextCount(2)
                .verifyComplete();
    }
}
