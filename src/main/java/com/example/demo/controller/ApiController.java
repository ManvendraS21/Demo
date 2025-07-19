package com.example.demo.controller;


import com.example.demo.service.QuoteService;
import com.example.demo.model.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
class ApiController {
    @Autowired
    private final QuoteService quoteService;
    public ApiController(QuoteService quoteService){
        this.quoteService= quoteService;
    }

    @GetMapping({"/quotes","/user/quotes"})
    public String showingAllQuotes(Model model){
        List<Quotes> quotes = (List<Quotes>) quoteService.findAll();
        model.addAttribute("quotes",quotes);
        return "quotes";
    }

    @GetMapping("/user/quotes/{id}")
    public String showSingleQuote(@PathVariable String id, Model model) {
        Optional<Quotes> quoteOptional = quoteService.findById(id);
        if (quoteOptional.isPresent()) {
            model.addAttribute("quote", quoteOptional.get());
            return "single-quote";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote not found with ID: " + id);
        }
    }

    @PostMapping("/user/quotes")
    public String makeQuote(@ModelAttribute Quotes quote){
        Quotes newQuote = quoteService.addQuote(quote);
        return "redirect:/user/quotes/" + newQuote.getId();
    }

    @PutMapping("/user/quotes/{id}")
    public String updateQuote(@PathVariable String id, @ModelAttribute Quotes quote){
        Quotes updatedQuote = quoteService.updateQuote(id, quote);
        return "redirect:/user/quotes/" + updatedQuote.getId();
    }

    @DeleteMapping("/user/quotes/{id}")
    public String deleteQuote(@PathVariable String id){
        quoteService.deleteQuote(id);
        return "redirect:/quotes";
    }
}
