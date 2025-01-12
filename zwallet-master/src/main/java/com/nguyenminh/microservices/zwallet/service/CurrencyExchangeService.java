package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.CurrencyExchangeRequest;
import com.nguyenminh.microservices.zwallet.model.CurrencyExchange;
import com.nguyenminh.microservices.zwallet.repository.CurrencyExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {


    private final CurrencyExchangeRepository currencyExchangeRepository;

    // Create new currency exchange
    public CurrencyExchange createCurrencyExchange(CurrencyExchange currencyExchange) {
        return currencyExchangeRepository.save(currencyExchange);
    }

    // Get all currency exchanges
    public List<CurrencyExchange> getAllCurrencyExchanges() {
        return currencyExchangeRepository.findAll();
    }

    // Get currency exchange by ID
    public Optional<CurrencyExchange> getCurrencyExchangeById(String id) {
        return currencyExchangeRepository.findById(id);
    }

    // Update existing currency exchange
    public CurrencyExchange updateCurrencyExchange(String id, CurrencyExchange updatedCurrencyExchange) {
        Optional<CurrencyExchange> existingExchange = currencyExchangeRepository.findById(id);
        if (existingExchange.isPresent()) {
            CurrencyExchange currencyExchange = existingExchange.get();
            currencyExchange.setCurrencyFrom(updatedCurrencyExchange.getCurrencyFrom());
            currencyExchange.setCurrencyTo(updatedCurrencyExchange.getCurrencyTo());
            currencyExchange.setConversionMultiple(updatedCurrencyExchange.getConversionMultiple());
            return currencyExchangeRepository.save(currencyExchange);
        } else {
            return null;  // Or throw an exception
        }
    }

    // Delete currency exchange by ID
    public void deleteCurrencyExchange(String id) {
        currencyExchangeRepository.deleteById(id);
    }

    // Create new currency exchange
    public Optional<CurrencyExchange> findAllMatchingCurrency(CurrencyExchangeRequest currencyExchange) {

        return currencyExchangeRepository.findByCurrencyFromAndCurrencyTo(
                currencyExchange.getFrom(),
                currencyExchange.getTo()
        );


    }



}
