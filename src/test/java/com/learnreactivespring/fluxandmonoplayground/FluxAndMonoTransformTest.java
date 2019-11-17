package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.TextScore;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam","abhi","tom");
    @Test
    public void transformUsingMap(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                                .map(s->s.toUpperCase())
                                .log();

        StepVerifier.create(namesFlux)
                    .expectNext("ADAM","ABHI","TOM")
                    .verifyComplete();

    }

    @Test
    public void transformUsingMap_repeat(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(s->s.toUpperCase())
                .repeat(1)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("ADAM","ABHI","TOM","ADAM","ABHI","TOM")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_filter(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s->s.length()>3)
                .map(s->s.toUpperCase())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("ADAM","ABHI")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap(){

        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap(s->{
                    return Flux.fromIterable(convertToList(s));
                }) //db or external service that returns a flux
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"newValue");

    }

    @Test
    public void transformUsingFlatMap_usingParallel(){

        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)   // Flux<Flux<String>>
                .flatMap((s)->
                    s.map(this::convertToList).subscribeOn(parallel()))
                    .flatMap(s->Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_usingParallel_maintainOrder(){

        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)   // Flux<Flux<String>>
                .flatMapSequential((s)->
                        s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(s->Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
