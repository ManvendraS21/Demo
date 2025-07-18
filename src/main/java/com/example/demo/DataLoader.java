package com.example.demo;


import com.example.demo.model.Quotes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
class DataLoader implements CommandLineRunner {
    private final QuotesRepository quotesRepository;

    public DataLoader(QuotesRepository quotesRepository){
        this.quotesRepository = quotesRepository;
    }

    public void run(String... args){
        if(quotesRepository.count()==0) {
            quotesRepository.saveAll(List.of(
                    Quotes.builder()
                            .id(String.valueOf(UUID.randomUUID()))
                            .quote("The only way to do great work is to love what you do.")
                            .author("Steve Jobs")
                            .date(LocalDate.of(2023, 1, 15))
                            .build(),
                    Quotes.builder()
                            .id(String.valueOf(UUID.randomUUID()))
                            .quote("Believe you can and you're halfway there.")
                            .author("Theodore Roosevelt")
                            .date(LocalDate.of(2023, 2, 20))
                            .build(),
                    Quotes.builder()
                            .id(String.valueOf(UUID.randomUUID()))
                            .quote("World is beautiful")
                            .author("Anonymous")
                            .date(LocalDate.now())
                            .build()
                    ));
        }
    }
}
