package com.example.demo.service;

import com.example.demo.repository.QuotesRepository;
import com.example.demo.model.Quotes;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService {

    private final QuotesRepository quotesRepository;

    public QuoteService(QuotesRepository quotesRepository){
        this.quotesRepository = quotesRepository;
    }

//    @Cacheable(value = "quotes")
    public Page<Quotes> findAll(Pageable pageable){
        return quotesRepository.findAll(pageable);
    }

    @Cacheable(value = "quotes", key = "#id")
    public Optional<Quotes> findById(String id){
        return quotesRepository.findById(id);
    }


    @CachePut(value = "quotes", key="#result.id")
    public Quotes addQuote(Quotes quotes){
        if(quotes.getId() == null || quotes.getId().isEmpty()){
            quotes.setId(String.valueOf(UUID.randomUUID()));
        }
        if(quotes.getAuthor() == null || quotes.getAuthor().isEmpty()){
            quotes.setAuthor("Anonymous");
        }
        if(quotes.getDate() == null ){
            quotes.setDate(LocalDate.now());
        }
        return quotesRepository.save(quotes);
    }

    @CachePut(value = "quotes", key = "#id")
    public Quotes updateQuote(String id,Quotes newQuote){
        return quotesRepository.findById(id).map(oldQuote ->{
            oldQuote.setQuote(newQuote.getQuote());

            if(newQuote.getAuthor()==null || newQuote.getAuthor().isEmpty()){
                oldQuote.setAuthor("Anonymous");
            } else if (!(newQuote.getAuthor().equals(oldQuote.getAuthor()))){
                oldQuote.setAuthor(newQuote.getAuthor());
            }

            oldQuote.setDate(LocalDate.now());
            return quotesRepository.save(oldQuote);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id does not exist: " + id));
    }

    @CacheEvict(value = "quotes", key = "#id")
    public void deleteQuote(String id){
        if(!quotesRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id does not exist: " + id);
        }
        quotesRepository.deleteById(id);
    }
}
