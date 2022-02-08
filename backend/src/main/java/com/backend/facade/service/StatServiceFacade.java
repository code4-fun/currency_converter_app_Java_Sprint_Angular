package com.backend.facade.service;

import com.backend.controller.dto.CurrencyDto;
import com.backend.domain.Currency;
import com.backend.domain.Stat;
import java.time.LocalDateTime;
import java.util.List;

public interface StatServiceFacade {
  Stat updateStat(Stat stat);
  Stat createStat(Stat stat);
  void deleteStat(Long id);
  Stat getStatById(Long id);
  List<Stat> getAllStat();
  void deleteAllStat();
  void saveStatistics(Currency curFrom, Currency curTo, Double amount,
                             Double exRate, LocalDateTime dateTime);
  List<CurrencyDto> getHistory();
  CurrencyDto convertStatToCurrencyDto(Stat stat);
  List<Stat> getStatBetweenTwoDates(LocalDateTime startDateTime, LocalDateTime endDateTime);
  List<CurrencyDto> getStatistics();
}