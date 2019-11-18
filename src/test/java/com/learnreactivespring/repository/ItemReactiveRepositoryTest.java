package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(new Item(null,"Samsumg TV",400.0),
            new Item(null,"LG TV",440.0),
            new Item(null,"Apple Watch",299.99),
            new Item(null,"Beats Headphones",149.99),
            new Item("ABC","Bose Headphones",149.99));

    @BeforeEach
    public  void setup(){
        itemReactiveRepository.deleteAll()
                            .thenMany(Flux.fromIterable(itemList)
                            .flatMap(itemReactiveRepository::save)
                            .doOnNext((item -> {
                                System.out.println("Inserted item is :"+item);
                            })))
                            .blockLast();

    }

    @Test
    public void getAllItems(){

        StepVerifier.create(itemReactiveRepository.findAll())
                    .expectSubscription()
                    .expectNextCount(5)
                    .verifyComplete();
    }

    @Test
    public void getItemById(){
        StepVerifier.create(itemReactiveRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches((item-> item.getDescription().equals("Bose Headphones")))
                .verifyComplete();
    }
}
