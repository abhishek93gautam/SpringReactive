package com.learnreactivespring.initialize;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
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


}
