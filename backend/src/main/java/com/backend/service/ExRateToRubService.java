package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import com.backend.repository.ExRateToRubRepository;
import com.backend.facade.service.ExRateToRubServiceFacade;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExRateToRubService implements ExRateToRubServiceFacade {
  @NonNull private final ExRateToRubRepository exRateToRubRepository;

  /**
   * Updates ExRateToRub object.
   */
  public ExRateToRub updateExRateToRub(ExRateToRub exRateToRub) {
    ExRateToRub exRateToRubToUpdate = getExRateToRubById(exRateToRub.getId());
    exRateToRubToUpdate.setDate(exRateToRub.getDate());
    exRateToRubToUpdate.setNominal(exRateToRub.getNominal());
    exRateToRubToUpdate.setValue(exRateToRub.getValue());
    exRateToRubToUpdate.setCurrency(exRateToRub.getCurrency());
    return createExRateToRub(exRateToRubToUpdate);
  }

  /**
   * Creates ExRateToRub object if there is no such object in the database.
   * Returns existing ExRateToRub object otherwise.
   * @param exRateToRub ExRateToRub to create
   * @return created or existing ExRateToRub object
   */
  public ExRateToRub createExRateToRub(ExRateToRub exRateToRub) {
    try{
      return getExRateToRubByCurrencyAndDate(exRateToRub.getCurrency(), exRateToRub.getDate());
    } catch (EntityNotFoundException e) {
      return exRateToRubRepository.save(exRateToRub);
    }
  }

  /**
   * Deletes the entity with the given id.
   */
  public void deleteExRateToRub(Long id) {
    exRateToRubRepository.deleteById(id);
  }

  /**
   * Retrieves ExRateToRub object by id.
   */
  public ExRateToRub getExRateToRubById(Long id) {
    return exRateToRubRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("There is no exchange " +
            "rate to rub with id=" + id));
  }

  /**
   * Returns all instances of ExRateToRub class.
   */
  public List<ExRateToRub> getAllExRateToRub() {
    return exRateToRubRepository.findAll();
  }

  /**
   * Deletes all instances of ExRateToRub class.
   */
  public void deleteAllExRateToRub(){
    exRateToRubRepository.deleteAll();
  }

  /**
   * Returns the exchange rate to ruble for the provided currency on a specified date.
   * @param currency currency
   * @param date date
   * @return exchange rate to ruble
   */
  public ExRateToRub getExRateToRubByCurrencyAndDate(Currency currency, LocalDate date){
    List<ExRateToRub> exRateToRub = exRateToRubRepository.findByCurrencyAndDate(currency, date);
    if(exRateToRub.size() == 1){
      return exRateToRub.get(0);
    } else {
      throw new EntityNotFoundException("There is no exchange rate to rub for " + currency +
          " on " + date);
    }
  }

  /**
   * Returns the exchange rate to ruble for the provided currency on a latest date.
   * @param currency currency
   * @return exchange rate to ruble
   */
  public ExRateToRub getExRateToRubByCurrencyAndLatestDate(Currency currency){
    List<ExRateToRub> exRateToRub = exRateToRubRepository
      .findByCurrencyAndLatestDate(currency.getId());
    if(exRateToRub.size() == 1){
      return exRateToRub.get(0);
    } else {
      throw new EntityNotFoundException("There is no exchange rate to rub for " + currency);
    }
  }

  public List<ExRateToRub> getAllExRateToRubOnLatestDate(){
    return exRateToRubRepository.findAllByLatestDate();
  }
}