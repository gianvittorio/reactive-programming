package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.util.CommonUtil;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.gianvittorio.reactor.util.CommonUtil.delay;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() {

        Flux<Integer> flux = Flux.range(1, 10);

        flux.subscribe(i -> System.out.println("Subscriber 1: " + i));

        flux.subscribe(i -> System.out.println("Subscriber 2: " + i));
    }

    @Test
    public void hotPublisherTest() {

        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1_000));

        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(i -> System.out.println("Subscriber 1: " + i));

        delay(4_000);

        connectableFlux.subscribe(i -> System.out.println("Subscriber 2: " + i));

        delay(10_000);
    }

    @Test
    public void hotPublisherAutoConnectTest() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1_000))
                .doFinally(signalType -> latch.countDown());

        Flux<Integer> hotSource = flux.publish()
                .autoConnect(2);

        hotSource.subscribe(i -> System.out.println("Subscriber 1: " + i));

        delay(2_000);

        hotSource.subscribe(i -> System.out.println("Subscriber 2: " + i));

        delay(2_000);

        hotSource.subscribe(i -> System.out.println("Subscriber 3: " + i));

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void hotPublisherRefCountTest() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1_000))
                .doOnCancel(() -> System.out.println("Received Cancel Signal"));

        Flux<Integer> hotSource = flux.publish()
                .refCount(2);

        Disposable disposable = hotSource.subscribe(i -> System.out.println("Subscriber 1: " + i));

        delay(2_000);

        Disposable disposable1 = hotSource.subscribe(i -> System.out.println("Subscriber 2: " + i));

        delay(2_000);

        disposable.dispose();
        disposable1.dispose();

        Disposable disposable2 = hotSource.subscribe(i -> System.out.println("Subscriber 3: " + i));

        delay(2_000);

        hotSource.subscribe(i -> System.out.println("Subscriber 4: " + i));

        delay(10_000);
    }
}
