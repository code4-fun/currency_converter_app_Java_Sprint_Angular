package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.Stat;
import com.backend.dto.HistoryDto;
import com.backend.dto.StatisticsDto;
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
   * Returns all history of currency conversions from the database.
   * @return list of HistoryDto objects
   */
  public List<HistoryDto> getHistory(){
    return getAllStat()
        .stream()
        .map(this::convertStatToHistoryDto)
        .collect(Collectors.toList());
  }

  /**
   * Converts Stat instance to HistoryDto instance.
   * @param stat Stat object
   * @return HistoryDto object
   */
  private HistoryDto convertStatToHistoryDto(Stat stat){
    NumberFormat formatter = new DecimalFormat("#.####");
    return HistoryDto.builder()
        .dateTime(stat.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
        .curFrom(stat.getCurFrom().getCharCode())
        .curTo(stat.getCurTo().getCharCode())
        .amount(formatter.format(stat.getAmount()))
        .exRate(formatter.format(stat.getExRate()))
        .sumConverted(formatter.format(stat.getAmount() * stat.getExRate()))
        .build();
  }

  /**
   * Retrieves statistics data from database between two dates.
   * @param startDateTime start date and time
   * @param endDateTime end date and time
   * @return
   */
  public List<Stat> getStatBetweenTwoDates(LocalDateTime startDateTime,
                                           LocalDateTime endDateTime){
    return statRepository.findByDateTimeBetween(startDateTime, endDateTime);
  }

  /**
   * Returns a list of objects containing statistics for the current week.
   * @return list of StatisticsDto objects
   */
  public List<StatisticsDto> getStatistics() {
    List<Stat> statOfCurrentWeek = getStatBetweenTwoDates(Utilities.startOfWeek(),
        Utilities.endOfWeek());

    List<StatisticsDto> res = new ArrayList<>();
    while (statOfCurrentWeek.size() != 0){
      StatisticsDto statDto = new StatisticsDto();
      Stat removed = statOfCurrentWeek.remove(0);
      statDto.setCurFrom(removed.getCurFrom().getCharCode());
      statDto.setCurTo(removed.getCurTo().getCharCode());
      statDto.setAmountConverted(removed.getAmount());
      statDto.setExRateSum(removed.getExRate());
      statDto.setCounter(1);
      Iterator<Stat> iterator = statOfCurrentWeek.iterator();
      while (iterator.hasNext()){
        Stat next = iterator.next();
        if(next.getCurFrom().getCharCode().equals(statDto.getCurFrom())
            && next.getCurTo().getCharCode().equals(statDto.getCurTo())){
          statDto.addValueToAmountConverted(next.getAmount());
          statDto.addValueToExRateSum(next.getExRate());
          statDto.incrementCounter();
          iterator.remove();
        }
      }
      statDto.calculateAverageExRate();
      res.add(statDto);
    }
    return res;
  }
}