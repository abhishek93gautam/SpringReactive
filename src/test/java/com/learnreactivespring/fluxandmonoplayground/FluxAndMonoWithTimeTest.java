package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux =  Flux.interval(Duration.ofMillis(200))
                                        .log();

        infiniteFlux.subscribe((element)->System.out.println("Value is : "+element));
        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() {
        Flux<Long> infiniteFlux =  Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux)
                    .expectSubscription()
                    .expectNext(0L,1L,2L)
                    .verifyComplete();
    }

    @Test
    public void infiniteSequenceMapTest() {
        Flux<Integer> infiniteFlux =  Flux.interval(Duration.ofMillis(200))
                .map(l->new Integer(l.intValue()))
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }
}
