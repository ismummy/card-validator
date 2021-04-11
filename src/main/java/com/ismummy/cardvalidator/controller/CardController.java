package com.ismummy.cardvalidator.controller;

import com.ismummy.cardvalidator.exception.InvalidPageException;
import com.ismummy.cardvalidator.helpers.CardCountResponse;
import com.ismummy.cardvalidator.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-scheme")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/")
    public String welcome() {
        return "Welcome";
    }

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity verifyCard(@PathVariable("cardNumber") String cardNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return new ResponseEntity<>(cardService.verifyCard(cardNumber, entity), HttpStatus.OK);
    }

    @GetMapping(value = "/stats", params = {"start", "limit"})
    public ResponseEntity<CardCountResponse> getCardStatistics(@RequestParam("start") int start, @RequestParam("limit") int limit) {
        if (start < 0) throw new InvalidPageException();
        if (start >= 1) start--;
        return new ResponseEntity<>(cardService.getCardsCount(PageRequest.of(start, limit)), HttpStatus.OK);
    }
}
