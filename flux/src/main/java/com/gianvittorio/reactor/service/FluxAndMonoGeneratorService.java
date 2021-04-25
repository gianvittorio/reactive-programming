package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
public class FluxAndMonoGeneratorService {
    public static void main(String[] args) {
        FluxAndMonoGeneratorService generatorService = new FluxAndMonoGeneratorService();

        generatorService.namesFlux()
                .subscribe(name -> System.out.format("Name: %s\n", name));

        generatorService.namesMono()
                .subscribe(name -> System.out.format("Mono Name: %s\n", name));
    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log();
    }

    public Mono<String> namesMono() {
        return Mono.just("alex")
                .log();
    }

    public Flux<String> namesFluxMap() {
        return namesFlux()
                .map(String::toUpperCase)
                .map(
                        name -> String.valueOf(name.length())
                                .concat(" - ")
                                .concat(name)
                )
                .doOnSubscribe(System.out::println)
                .doOnNext(name -> System.out.println("Name: " + name))
                .doOnComplete(() -> System.out.println("Inside the complete callback"))
                .doFinally(signalType -> System.out.println("Inside doFInally: " + signalType));
    }

    public Flux<String> namesFluxImmutable() {
        Flux<String> namesFlux = namesFlux();
        namesFlux.map(String::toUpperCase);

        return namesFlux;
    }

    public Flux<String> nameFluxFilterGreaterThan(int length) {
        return namesFlux()
                .filter(name -> name.length() > length);
    }

    public Mono<String> nameMonoMapFilter(int length) {
        final String defaultName = "7 - DEFAULT";

        return Mono.just("alex")
                .filter(name -> name.length() > length)
                .map(String::toUpperCase)
                .map(
                        name -> Integer.toString(name.length())
                                .concat(" - ")
                                .concat(name)
                )
                .defaultIfEmpty(defaultName);
    }

    public Mono<String> nameMonoMapFilterSwitchIfEmpty(int length) {
        UnaryOperator<Mono<String>> operationChain =
                input -> input.filter(name -> name.length() > length)
                        .map(String::toUpperCase)
                        .map(
                                name -> Integer.toString(name.length())
                                        .concat(" - ")
                                        .concat(name)
                        );

        return Mono.just("alex")
                .transform(operationChain)
                .switchIfEmpty(Mono.just("default").transform(operationChain));
    }

    public Mono<List<String>> nameMonoFlatMap(int length) {
        return namesMono()
                .filter(name -> name.length() > length)
                .map(String::toUpperCase)
                .flatMap(Utils::splitStringMono);
    }

    public Flux<String> nameMonoFlatMapMany(int length) {
        return namesMono()
                .filter(name -> name.length() > length)
                .map(String::toUpperCase)
                .flatMapMany(Utils::splitString);
    }

    public Mono<String> nameMonoFilterGreaterThan(int length) {
        return namesMono()
                .filter(name -> name.length() > length);
    }

    public Flux<String> namesFluxTransform(int length) {

        Function<? super Flux<String>, ? extends Flux<String>> filterMap =
                name -> name.map(String::toUpperCase)
                        .filter(s -> s.length() > length)
                        .flatMap(Utils::splitString);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .defaultIfEmpty("default");
    }

    public Flux<String> namesFluxTransformSwitchIfEmpty(int length) {

        Function<? super Flux<String>, ? extends Flux<String>> filterMap =
                name -> name.map(String::toUpperCase)
                        .filter(s -> s.length() > length)
                        .flatMap(Utils::splitString);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(Flux.just("default").transform(filterMap));
    }

    public Flux<String> namesFluxFlatMap(int length) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .filter(name -> name.length() > length)
                .map(String::toUpperCase)
                .flatMap(Utils::splitString);
    }

    public Flux<String> namesFluxFlatMapAsync(int length) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .filter(name -> name.length() > length)
                .map(String::toUpperCase)
                .flatMap(Utils::splitStringWithDelay);
    }

    public Flux<String> namesFluxConcatMap(int length) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .filter(name -> name.length() > length)
                .map(String::toUpperCase)
                .concatMap(Utils::splitStringWithDelay);
    }

    public Flux<String> exploreConcat() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux);
    }

    public Flux<String> exploreConcatWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux);
    }

    public Flux<String> exploreConcatWithMono() {
        Mono<String> aMono = Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.concatWith(bMono);
    }

    public Flux<String> exploreMerge() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux);
    }

    public Flux<String> exploreMergeWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux);
    }

    public Flux<String> exploreMergeWithMono() {
        Mono<String> aMono = Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.mergeWith(bMono);
    }

    public Flux<String> exploreMergeSequential() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux);
    }

    public Flux<String> exploreZip() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (lhs, rhs) -> lhs.concat(rhs));
    }

    public Flux<String> exploreZip1() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        Flux<String> flux3 = Flux.just("1", "2", "3");

        Flux<String> flux4 = Flux.just("4", "5", "6");


        return Flux.zip(abcFlux, defFlux, flux3, flux4)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4());
    }

    public Flux<String> exploreZipWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (lhs, rhs) -> lhs.concat(rhs));
    }

    public Mono<String> exploreZipWithMono() {
        Mono<String> aMono = Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2());
    }

    public Flux<String> exceptionFlux() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"));
    }

    public Flux<String> exploreOnErrorReturn() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception occurred")))
                .onErrorReturn("D");
    }

    public Flux<String> exploreOnErrorResume(Exception e) {

        Flux<String> recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("Exception is: " + ex);

                    if (ex instanceof IllegalStateException) {
                        return recoveryFlux;
                    }

                    return Flux.error(ex);
                });
    }

    public Flux<String> exploreOnErrorContinue(Exception e) {

        Flux<String> recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if (name.equals("B")) {
                        throw new IllegalStateException("Exception occurred");
                    }

                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex, value) -> {
                    log.error("Exception is: " + ex);
                    log.info("value is: " + value);
                });
    }

    public Mono<String> exploreOnErrorContinueMono(String input) {

        return Mono.just(input)
                .map(name -> {
                    if (name.equals("abc")) {
                        throw new RuntimeException("Exception occurred");
                    }
                    return name;
                })
                .onErrorContinue((ex, name) -> {
                    log.error("Exception: " + ex);
                    log.info("name: " + name);
                });
    }

    public Flux<String> exploreOnErrorMap() {

        Flux<String> recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if (name.equals("B")) {
                        throw new IllegalStateException("Exception occurred");
                    }

                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorMap(ex -> {
                    log.error("Exception is: " + ex);

                    return new ReactorException(ex.getMessage(), ex);
                });
    }

    public Flux<String> exploreDoOnError() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception occurred")))
                .doOnError(ex -> {
                    log.error("Exception is: " + ex);
                });
    }

    public Mono<Object> exploreDoOnErrorMono() {
        return Mono.just("A")
                .map(value -> {
                    throw new RuntimeException("Exception occurred");
                })
                .onErrorReturn("ABC");
    }

    public interface Utils {
        static Flux<String> splitString(String s) {
            return Flux.fromStream(
                    s.chars()
                            .mapToObj(c -> (char) c)
                            .map(String::valueOf)
            );
        }

        static Flux<String> splitStringWithDelay(String s) {
            final int delay = 1000;

            return splitString(s)
                    .delayElements(Duration.ofMillis(delay));
        }

        static Mono<List<String>> splitStringMono(String s) {
            return Mono.just(
                    s.chars()
                            .mapToObj(c -> (char) c)
                            .map(String::valueOf)
                            .collect(Collectors.toList())
            );
        }
    }
}
