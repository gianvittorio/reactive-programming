package com.gianvittorio.reactor.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
}
