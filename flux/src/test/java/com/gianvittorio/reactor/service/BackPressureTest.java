package com.gianvittorio.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BackPressureTest {

    @Test
    public void exploreBackPressureTest() {
        Flux<Integer> numberRange = Flux.range(1, 100)
                .log();

        numberRange.subscribe(
                new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("value is: {}", value);

                        if (Integer.compare(value, 2) == 0) {
                            cancel();
                        }
                    }
                }
        );
    }

    @Test
    public void exploreBackPressure1Test() throws InterruptedException {
        Flux<Integer> numberRange = Flux.range(1, 100)
                .log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange.subscribe(
                new BaseSubscriber<Integer>() {

                    private int cnt = 0;

                    private static final int MAX = 2;

                    private void incrementCountAndRequest(Integer value) {
                        ++cnt;

                        if (Integer.compare(MAX, cnt) == 0 || Integer.compare(value, 50) < 0) {
                            request(MAX);

                            cnt %= MAX;
                        } else {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("value is: {}", value);

                        incrementCountAndRequest(value);
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");

                        latch.countDown();
                    }
                }
        );

        org.assertj.core.api.Assertions.assertThat(latch.await(5L, TimeUnit.SECONDS))
                .isTrue();
    }

    @Test
    public void exploreBackPressureDropTest() throws InterruptedException {
        Flux<Integer> numberRange = Flux.range(1, 100)
                .onBackpressureDrop(item -> {
                    log.info("Dropped items are: {}", item);
                })
                .log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange.subscribe(
                new BaseSubscriber<Integer>() {

                    private int cnt = 0;

                    private static final int MAX = 2;

                    private void incrementCountAndRequest(Integer value) {
                        ++cnt;

//                        if (Integer.compare(MAX, cnt) == 0 || Integer.compare(value, 50) < 0) {
//                            request(MAX);
//
//                            cnt %= MAX;
//                        } else {
//                            cancel();
//                        }

                        if (value == 2) {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("value is: {}", value);

                        incrementCountAndRequest(value);
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");

                        latch.countDown();
                    }
                }
        );

        org.assertj.core.api.Assertions.assertThat(latch.await(5L, TimeUnit.SECONDS))
                .isTrue();
    }

    @Test
    public void exploreBackPressureBufferTest() throws InterruptedException {
        Flux<Integer> numberRange = Flux.range(1, 100)
                .onBackpressureBuffer(10, i -> {
                    log.info("Last buffered element is: {}", i);
                })
                .log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange.subscribe(
                new BaseSubscriber<Integer>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("value is: {}", value);

                        if (value < 50) {
                            request(1);
                        } else {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");

                        latch.countDown();
                    }
                }
        );

        org.assertj.core.api.Assertions.assertThat(latch.await(5L, TimeUnit.SECONDS))
                .isTrue();
    }

    @Test
    public void exploreBackPressureErrorTest() throws InterruptedException {
        Flux<Integer> numberRange = Flux.range(1, 100)
                .onBackpressureError()
                .log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange.subscribe(
                new BaseSubscriber<Integer>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("value is: {}", value);

                        if (value < 50) {
                            request(1);
                        } else {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");

                        latch.countDown();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        log.error("Exception is: {}", throwable);
                    }
                }
        );

        org.assertj.core.api.Assertions.assertThat(latch.await(5L, TimeUnit.SECONDS))
                .isTrue();
    }
}
