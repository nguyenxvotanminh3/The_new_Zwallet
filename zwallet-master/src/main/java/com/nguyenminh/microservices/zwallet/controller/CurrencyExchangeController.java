package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.dto.CurrencyCaculate;
import com.nguyenminh.microservices.zwallet.dto.CurrencyExchangeRequest;
import com.nguyenminh.microservices.zwallet.model.CurrencyExchange;
import com.nguyenminh.microservices.zwallet.service.CurrencyConversionService;
import com.nguyenminh.microservices.zwallet.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/currency-exchange")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CurrencyExchangeController {


    private final CurrencyExchangeService service;
    private final CurrencyConversionService currencyCalculate;

    // Create a new currency exchange
    @PostMapping
    public ResponseEntity<CurrencyExchange> createCurrencyExchange(@RequestBody CurrencyExchange currencyExchange) {
        CurrencyExchange createdExchange = service.createCurrencyExchange(currencyExchange);
        return ResponseEntity.ok(createdExchange);
    }

    // Get all currency exchanges
    @GetMapping
    public ResponseEntity<List<CurrencyExchange>> getAllCurrencyExchanges() {
        List<CurrencyExchange> currencyExchanges = service.getAllCurrencyExchanges();
        return ResponseEntity.ok(currencyExchanges);
    }

    // Get currency exchange by ID
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyExchange> getCurrencyExchangeById(@PathVariable String id) {
        Optional<CurrencyExchange> currencyExchange = service.getCurrencyExchangeById(id);
        return currencyExchange.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update currency exchange
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyExchange> updateCurrencyExchange(@PathVariable String id, @RequestBody CurrencyExchange currencyExchange) {
        CurrencyExchange updatedExchange = service.updateCurrencyExchange(id, currencyExchange);
        if (updatedExchange != null) {
            return ResponseEntity.ok(updatedExchange);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete currency exchange
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrencyExchange(@PathVariable String id) {
        service.deleteCurrencyExchange(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/hehe")
    public ResponseEntity<?> currencyCalculate(@RequestBody CurrencyExchangeRequest currencyExchangeRequest) {
       return currencyCalculate.currencyCalculate(currencyExchangeRequest);
    }

}