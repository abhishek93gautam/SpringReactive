package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest(){

        Flux<String> stringFlux = Flux.just("Spring","Spring boot","Reactive spring")
                                    .log();
        stringFlux
                .subscribe(System.out::println);

    }

    @Test
    public void fluxTestElements_WithoutError(){
        Flux<String> stringFlux = Flux.just("Spring","Spring boot","Reactive spring")
                .log();

        StepVerifier.create(stringFlux)
                    .expectNext("Spring")
                    .expectNext("Spring boot")
                    .expectNext("Reactive spring")
                    .verifyComplete();
    }

    @Test
    public void fluxTestElements_WithError(){
        Flux<String> stringFlux = Flux.just("Spring","Spring boot","Reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception occured.")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoTest(){

        Mono<String> stringMono = Mono.just("Spring")
                                        .log();
        StepVerifier.create(stringMono)
                    .expectNext("Spring")
                    .verifyComplete();
    }
}
