package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import com.backend.repository.CurrencyRepository;
import com.backend.facade.service.CurrencyServiceFacade;
import com.backend.facade.service.ExRateToRubServiceFacade;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService implements CurrencyServiceFacade {
  @NonNull private final CurrencyRepository currencyRepository;
  @NonNull private final ExRateToRubServiceFacade exRateToRubService;

  /**
   * Updates Currency object.
   */
  public Currency updateCurrency(Currency currency) {
    Currency currencyToUpdate = getCurrencyById(currency.getId());
    currencyToUpdate.setNumCode(currency.getNumCode());
    currencyToUpdate.setCharCode(currency.getCharCode());
    currencyToUpdate.setName(currency.getName());
    return createCurrency(currencyToUpdate);
  }

  /**
   * Creates Currency object if there is no such object in the database.
   * Returns existing Currency object otherwise.
   * @param currency Currency to create
   * @return created or existing Currency object
   */
  public Currency createCurrency(Currency currency) {
    try{
      return getCurrencyById(currency.getId());
    } catch (EntityNotFoundException e) {
      return currencyRepository.save(currency);
    }
  }

  /**
   * Deletes the entity with the given id.
   */
  public void deleteCurrency(String id) {
    currencyRepository.deleteById(id);
  }

  /**
   * Retrieves Currency object by id.
   */
  public Currency getCurrencyById(String id) {
    return currencyRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("There is no currency " +
            "with id=" + id));
  }

  /**
   * Retrieves Currency object by charCode.
   */
  public Currency getCurrencyByCharCode(String charCode){
    if(currencyRepository.findByCharCode(charCode).size() > 0){
      return currencyRepository.findByCharCode(charCode).get(0);
    } else {
      throw new EntityNotFoundException("There is no currency with charCode=" + charCode);
    }
  }

  /**
   * Returns all instances of Currency class.
   */
  public List<Currency> getAllCurrencies() {
    return currencyRepository.findAll();
  }

  /**
   * Deletes all instances of Currency class.
   */
  public void deleteAllCurrencies(){
    currencyRepository.deleteAll();
  }

  /**
   * Returns char codes of all currencies on the latest date.
   * @return list of char codes
   */
  public List<String> getAllCurrenciesCharCodes(){
    return exRateToRubService
        .getAllExRateToRubOnLatestDate()
        .stream()
        .map(ExRateToRub::getCurrency)
        .map(Currency::getCharCode)
        .collect(Collectors.toList());
  }
}