package com.learnreactivespring.controller.v1;

import com.learnreactivespring.constants.ItemConstants;
import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data(){
        return Arrays.asList(new Item(null,"Samsung TV",399.99),
                new Item(null,"LG TV",329.99),
                new Item(null,"Apple watch",349.99),
                new Item("ABC","Beats Headphones",199.99));
    }

    @BeforeEach
    public void setup(){
        itemReactiveRepository.deleteAll()
                            .thenMany(Flux.fromIterable(data()))
                            .flatMap(itemReactiveRepository::save)
                            .doOnNext((item)->{
                                System.out.println("Inserted item is :"+item);
                            })
                            .blockLast();
    }

    @Test
    public void getAllItems(){

        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectBodyList(Item.class)
                        .hasSize(4);
    }

    @Test
    public void getAllItems_approach2(){

        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith((response)->{
                    List<Item> items = response.getResponseBody();
                    items.forEach((item)->{
                        assertTrue(item.getId()!=null);
                    });
                });
    }

    @Test
    public void getAllItems_approach3(){

        Flux<Item> itemFlux =  webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemFlux)
                    .expectSubscription()
                    .expectNextCount(4)
                    .verifyComplete();
    }



}
