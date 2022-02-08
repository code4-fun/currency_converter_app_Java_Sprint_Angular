package com.backend.facade.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import java.io.IOException;
import java.time.LocalDate;

public interface DataProcessingServiceFacade {
  void parse() throws IOException;
  Currency saveCurrencyToDb(String id, String numCode, String charCode, String name);
  ExRateToRub saveExRateToRubToDb(LocalDate dateOfRate, Long nominal,Double value,
                                  Currency currency);
}
