package com.example.demo;

import com.example.demo.model.Quotes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Iterable<Quotes> findAll(){
        return (List<Quotes>) quotesRepository.findAll();
    }

    public Optional<Quotes> findById(String id){
        return quotesRepository.findById(id);
    }

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

    public Quotes updateQuote(String id,Quotes newQuote){
        return quotesRepository.findById(id).map(oldQuote ->{
            oldQuote.setQuote(newQuote.getQuote());

            if(!(newQuote.getAuthor()==null || newQuote.getAuthor().isEmpty()) && oldQuote.getAuthor().equals("Anonymous")){
                oldQuote.setAuthor(newQuote.getAuthor());
            }

            return quotesRepository.save(oldQuote);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id does not exist: " + id));
    }

    public void deleteQuote(String id){
        if(!quotesRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id does not exist: " + id);
        }
        quotesRepository.deleteById(id);
    }
}
