package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> names = Arrays.asList("adam","abhi","tom");
    @Test
    public void filterTest(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                                .filter(s->s.startsWith("a"))
                                .log();

        StepVerifier.create(namesFlux)
                    .expectNext("adam","abhi")
                    .verifyComplete();
    }
}
