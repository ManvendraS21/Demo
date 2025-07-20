package com.example.demo.controller;


import com.example.demo.service.QuoteService;
import com.example.demo.model.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Controller
class ApiController {
    @Autowired
    private final QuoteService quoteService;
    public ApiController(QuoteService quoteService){
        this.quoteService= quoteService;
    }

    @GetMapping({"/quotes","/user/quotes"})
    public String showingAllQuotes(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "date") String sortBy, // New: for sorting
                                   @RequestParam(defaultValue = "desc") String sortDir) { // New: for sort direction

        // Create a Pageable object with pagination and sorting information
        // PageRequest.of(page, size, Sort.by(Sort.Direction.ASC/DESC, sortByField))
        Pageable pageable;
        if (sortDir.equalsIgnoreCase("asc")) {
            pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy).descending());
        }


        Page<Quotes> quotesPage = quoteService.findAll(pageable);
        model.addAttribute("quotes", quotesPage.getContent()); // Get the list of quotes for the current page
        model.addAttribute("currentPage", quotesPage.getNumber());
        model.addAttribute("totalPages", quotesPage.getTotalPages());
        model.addAttribute("totalItems", quotesPage.getTotalElements());
        model.addAttribute("pageSize", quotesPage.getSize());
        model.addAttribute("hasPrevious", quotesPage.hasPrevious());
        model.addAttribute("hasNext", quotesPage.hasNext());
        model.addAttribute("sortField", sortBy);
        model.addAttribute("sortDirection", sortDir);


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
