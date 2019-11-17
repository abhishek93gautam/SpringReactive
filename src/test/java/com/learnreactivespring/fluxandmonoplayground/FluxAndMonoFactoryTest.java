package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam","abhi","tom");
    @Test
    public void fluxUsingIterable(){
        Flux<String> namesFlux =  Flux.fromIterable(names);

        StepVerifier.create(namesFlux)
                    .expectNext("adam","abhi","tom")
                    .verifyComplete();
    }

    @Test
    public void fluxUsingArray(){

        String[] names = new String[] {"a","b","c"};

        Flux<String> namesFlux = Flux.fromArray(names).log();

        StepVerifier.create(namesFlux)
                    .expectNext("a","b","c")
                    .verifyComplete();
    }

    @Test
    public void fluxUsingStream(){
        Flux<String> namesFlux = Flux.fromStream(names.stream())
                                    .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","abhi","tom")
                .verifyComplete();

    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> mono =  Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){

        Supplier<String> stringSupplier = () -> "adam";

        Mono<String> mono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(mono)
                    .expectNext("adam")
                    .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){

        Flux<Integer> integerFlux =  Flux.range(1,5);

        StepVerifier.create(integerFlux).expectNext(1,2,3,4,5)
                    .verifyComplete();
    }
}
