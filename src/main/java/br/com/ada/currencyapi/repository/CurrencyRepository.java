package br.com.ada.currencyapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ada.currencyapi.domain.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    Currency findByName(String name);
}
