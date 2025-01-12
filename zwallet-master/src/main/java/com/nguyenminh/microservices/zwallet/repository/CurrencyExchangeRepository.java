package com.nguyenminh.microservices.zwallet.repository;

import com.nguyenminh.microservices.zwallet.model.CurrencyExchange;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyExchangeRepository extends MongoRepository<CurrencyExchange, String> {
    Optional<CurrencyExchange> findByCurrencyFromAndCurrencyTo(String currency_from, String currency_to);
}
