package com.backend.facade.domain;

import com.backend.domain.Currency;
import java.time.LocalDate;

public interface ExRateToRubFacade {
  Long getId();
  LocalDate getDate();
  Long getNominal();
  Double getValue();
  Currency getCurrency();
}