package com.example.demo.helper;


import com.example.demo.model.Quotes;
import com.example.demo.repository.QuotesRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
class DataLoader implements CommandLineRunner {
    private final QuotesRepository quotesRepository;
    private Faker faker;
    private static final int Number_of_Entries = 500;
    private static final int BATCH_SIZE = 500;
    public DataLoader(QuotesRepository quotesRepository){
        this.quotesRepository = quotesRepository;
        this.faker= new Faker(new Locale("en-US"));
    }

    @Override
    @Transactional
    public void run(String... args){
        if(quotesRepository.count() < 10){
            List<Quotes> initialQuotes = new ArrayList<>();
            for(int i=0;i<Number_of_Entries;i++){
                Quotes quote = new Quotes();
                quote.setAuthor(faker.book().author());
                quote.setQuote(faker.hobbit().quote());
                quote.setId(UUID.randomUUID().toString());
                Date randomBirthDateUtil = faker.date().birthday();
                LocalDate randomDate = randomBirthDateUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                quote.setDate(randomDate);
                initialQuotes.add(quote);
                if (initialQuotes.size() == BATCH_SIZE || i == Number_of_Entries - 1) {
                    quotesRepository.saveAll(initialQuotes);
                    System.out.println("Inserted " + initialQuotes.size() + " quotes. Total so far: " + (i + 1));
                    initialQuotes.clear();
                }

            }

        }
    }
}
