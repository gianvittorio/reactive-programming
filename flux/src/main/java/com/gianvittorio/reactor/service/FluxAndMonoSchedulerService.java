package com.gianvittorio.reactor.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.gianvittorio.reactor.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoSchedulerService {

    final static List<String> namesList = List.of("alex", "ben", "chloe");

    final static List<String> nameslist1 = List.of("adam", "jill", "jack");

    public Flux<String> explorePublishOn() {
        Flux<String> namesFlux = Flux.fromIterable(namesList)
                .publishOn(Schedulers.parallel())
                .map(this::uppercase)
                .doOnNext(name -> log.info("name is: {}", name));

        Flux<String> namesFlux1 = Flux.fromIterable(nameslist1)
                .publishOn(Schedulers.parallel())
                .map(this::uppercase)
                .doOnNext(name -> log.info("name is: {}", name));

        return namesFlux.mergeWith(namesFlux1);
    }

    public Flux<String> exploreSubscribeOn() {
        Flux<String> namesFlux = getMap()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(name -> log.info("name is: {}", name));

        Flux<String> namesFlux1 = getMap()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(name -> log.info("name is: {}", name));

        return namesFlux.mergeWith(namesFlux1);
    }

    private Flux<String> getMap() {
        return Flux.fromIterable(namesList)
                .map(this::uppercase);
    }

    private String uppercase(String name) {
        delay(1000);

        return name.toUpperCase();
    }
}
