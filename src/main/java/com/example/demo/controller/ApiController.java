package com.example.demo.controller;


import com.example.demo.QuoteService;
import com.example.demo.model.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class ApiController {
    @Autowired
    private final QuoteService quoteService;
    public ApiController(QuoteService quoteService){
        this.quoteService= quoteService;
    }

    @GetMapping({"/quotes","/user/quotes"})
    public ResponseEntity<List<Quotes>> getQuotes(){
        List<Quotes> quotes = (List<Quotes>) quoteService.findAll();
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("user/quotes/{id}")
    public ResponseEntity<Quotes> getQuotes(@PathVariable String id){
        return quoteService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("user/quotes")
    public ResponseEntity<Quotes> makeQuote(@RequestBody Quotes quote){
        Quotes newQuote = quoteService.addQuote(quote);
        return ResponseEntity.status(HttpStatus.CREATED).body(newQuote);
    }

    @PutMapping("user/quotes/{id}")
    public ResponseEntity<Quotes> updateQuote(@PathVariable String id, @RequestBody Quotes quote){
       Quotes newQuote = quoteService.updateQuote(id,quote);

       return ResponseEntity.ok(newQuote);

    }

    @DeleteMapping("admin/quotes/{id}")
    public void deleteQuote(@PathVariable String id){
        quoteService.deleteQuote(id);
    }
}
