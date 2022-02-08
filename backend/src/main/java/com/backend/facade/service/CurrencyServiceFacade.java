package com.backend.facade.service;

import com.backend.domain.Currency;
import java.util.List;

public interface CurrencyServiceFacade {
  Currency updateCurrency(Currency currency);
  Currency createCurrency(Currency currency);
  void deleteCurrency(String id);
  Currency getCurrencyById(String id);
  Currency getCurrencyByCharCode(String charCode);
  List<Currency> getAllCurrencies();
  void deleteAllCurrencies();
  List<String> getAllCurrenciesCharCodes();
}