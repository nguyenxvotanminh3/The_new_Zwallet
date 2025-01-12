package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.CurrencyCaculate;
import com.nguyenminh.microservices.zwallet.dto.CurrencyExchangeRequest;
import com.nguyenminh.microservices.zwallet.model.CurrencyExchange;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {
    private final CurrencyExchangeService currencyExchangeService;

    public ResponseEntity<?> currencyCalculate(CurrencyExchangeRequest currencyExchangeRequest) {


        Optional<CurrencyExchange> currencyExchange = currencyExchangeService.findAllMatchingCurrency(currencyExchangeRequest);

        if (currencyExchange.isPresent()) {
            CurrencyExchange exchange = currencyExchange.get();
            BigDecimal conversionMultiple = exchange.getConversionMultiple();
            BigDecimal convertedAmount = currencyExchangeRequest.getAmount()
                    .multiply(conversionMultiple);
            CurrencyCaculate result = new CurrencyCaculate();
            result.setResult(convertedAmount);
            return ResponseEntity.ok(result);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }
}
