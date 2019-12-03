package com.learnreactivespring.initialize;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemReactiveCappedRepository;
import com.learnreactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
@Slf4j
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    ItemReactiveCappedRepository itemReactiveCappedRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
        createCappedCollection();
        dataSetupForCappedCollection();
    }

    private void createCappedCollection() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
    }

    public List<Item> data(){
        return Arrays.asList(new Item(null,"Samsung TV",399.99),
                new Item(null,"LG TV",329.99),
                new Item(null,"Apple watch",349.99),
                new Item("ABC","Beats Headphones",199.99));
    }

    private void initialDataSetup() {

        itemReactiveRepository.deleteAll()
                    .thenMany(Flux.fromIterable(data()))
                    .flatMap(itemReactiveRepository::save)
                    .thenMany(itemReactiveRepository.findAll())
                    .subscribe((item)->{
                        System.out.println("Item inserted from command line runner");
                    });

    }

    public void dataSetupForCappedCollection(){

        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                    .map(i-> new ItemCapped(null,"Random Item "+i,(100.00+i)));

        itemReactiveCappedRepository.insert(itemCappedFlux)
                                    .subscribe(itemCapped -> {
                                        log.info("Inserted item is "+itemCapped);
                                    });

    }


}
