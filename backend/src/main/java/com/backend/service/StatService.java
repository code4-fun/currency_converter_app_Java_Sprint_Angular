package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.Stat;
import com.backend.controller.dto.CurrencyDto;
import com.backend.repository.StatRepository;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatService {
  @NonNull private final StatRepository statRepository;

  /**
   * Updates Stat object.
   */
  public Stat updateStat(Stat stat) {
    Stat statToUpdate = getStatById(stat.getId());
    statToUpdate.setCurFrom(stat.getCurFrom());
    statToUpdate.setCurTo(stat.getCurTo());
    statToUpdate.setAmount(stat.getAmount());
    statToUpdate.setExRate(stat.getExRate());
    statToUpdate.setDateTime(stat.getDateTime());
    return createStat(statToUpdate);
  }

  /**
   * Creates Stat object.
   */
  public Stat createStat(Stat stat) {
    return statRepository.save(stat);
  }

  /**
   * Deletes the entity with the given id.
   */
  public void deleteStat(Long id) {
    statRepository.deleteById(id);
  }

  /**
   * Retrieves Stat object by id.
   */
  public Stat getStatById(Long id) {
    return statRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("There is no statistic data " +
        "with id=" + id));
  }

  /**
   * Returns all instances of Stat class.
   */
  public List<Stat> getAllStat() {
    return statRepository.findAll();
  }

  /**
   * Deletes all instances of Stat class.
   */
  public void deleteAllStat(){
    statRepository.deleteAll();
  }

  /**
   * Saves the result of currency conversion to the database.
   * @param curFrom currency to be converted
   * @param curTo currency to convert to
   * @param amount amount of currency to be converted
   * @param exRate exchange rate
   * @param dateTime date and time of the operation
   */
  public void saveStatistics(Currency curFrom, Currency curTo, Double amount,
                             Double exRate, LocalDateTime dateTime){
    Stat stat = Stat.builder()
      .curFrom(curFrom)
      .curTo(curTo)
      .amount(amount)
      .exRate(exRate)
      .dateTime(dateTime)
      .build();
    statRepository.save(stat);
  }

  /**
   * Returns the list of CurrencyDto objects, representing all the history
   * of currency conversions.
   * This list is used as the return value of /history endpoint.
   * @return list of CurrencyDto objects containing the following fields:
   * dateTime, curFrom, curTo, sumBeforeConversion, exRate, sumAfterConversion
   */
  public List<CurrencyDto> getHistory(){
    return getAllStat()
      .stream()
      .map(this::convertStatToCurrencyDto)
      .collect(Collectors.toList());
  }

  /**
   * Converts a Stat instance to a CurrencyDto instance to be used
   * to generate a /history endpoint return value.
   * @param stat Stat object
   * @return CurrencyDto object
   */
  private CurrencyDto convertStatToCurrencyDto(Stat stat){
    NumberFormat formatter = new DecimalFormat("#.####");
    return CurrencyDto.builder()
      .dateTime(stat.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
      .curFrom(stat.getCurFrom().getCharCode())
      .curTo(stat.getCurTo().getCharCode())
      .sumBeforeConversion(Precision.round(stat.getAmount(), 4))
      .exRate(Precision.round(stat.getExRate(), 4))
      .sumAfterConversion(Precision.round(stat.getAmount() * stat.getExRate(), 4))
      .build();
  }

  /**
   * Retrieves statistics data from the database between two dates.
   * @param startDateTime start date and time
   * @param endDateTime end date and time
   * @return list of Stat objects
   */
  public List<Stat> getStatBetweenTwoDates(LocalDateTime startDateTime,
                                           LocalDateTime endDateTime){
    return statRepository.findByDateTimeBetween(startDateTime, endDateTime);
  }

  /**
   * Returns a list of CurrencyDto objects containing statistics for the current week.
   * This list is used as the return value of /stat endpoint.
   * @return list of CurrencyDto objects containing the following fields:
   * curFrom, curTo, sumBeforeConversion, averageExRate
   */
  public List<CurrencyDto> getStatistics() {
    List<Stat> statOfCurrentWeek = getStatBetweenTwoDates(Utilities.startOfWeek(),
      Utilities.endOfWeek());

    List<CurrencyDto> res = new ArrayList<>();
    while (statOfCurrentWeek.size() != 0){
      CurrencyDto currencyDto = new CurrencyDto();

      Stat removed = statOfCurrentWeek.remove(0);

      currencyDto.setCurFrom(removed.getCurFrom().getCharCode());
      currencyDto.setCurTo(removed.getCurTo().getCharCode());
      currencyDto.setSumBeforeConversion(removed.getAmount());
      currencyDto.setExRateSum(removed.getExRate());
      currencyDto.setCounter(1);

      Iterator<Stat> iterator = statOfCurrentWeek.iterator();
      while (iterator.hasNext()){
        Stat next = iterator.next();
        if(next.getCurFrom().getCharCode().equals(currencyDto.getCurFrom())
          && next.getCurTo().getCharCode().equals(currencyDto.getCurTo())){
          currencyDto.addValueToAmountConverted(next.getAmount());
          currencyDto.addValueToExRateSum(next.getExRate());
          currencyDto.incrementCounter();
          iterator.remove();
        }
      }
      currencyDto.calculateAverageExRate();
      res.add(currencyDto);
    }
    return res;
  }
}