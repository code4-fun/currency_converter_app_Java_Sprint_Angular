package com.backend.facade.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import java.time.LocalDate;
import java.util.List;

public interface ExRateToRubServiceFacade {
  ExRateToRub updateExRateToRub(ExRateToRub exRateToRub);
  ExRateToRub createExRateToRub(ExRateToRub exRateToRub);
  void deleteExRateToRub(Long id);
  ExRateToRub getExRateToRubById(Long id);
  List<ExRateToRub> getAllExRateToRub();
  void deleteAllExRateToRub();
  ExRateToRub getExRateToRubByCurrencyAndDate(Currency currency, LocalDate date);
  ExRateToRub getExRateToRubByCurrencyAndLatestDate(Currency currency);
  List<ExRateToRub> getAllExRateToRubOnLatestDate();
}