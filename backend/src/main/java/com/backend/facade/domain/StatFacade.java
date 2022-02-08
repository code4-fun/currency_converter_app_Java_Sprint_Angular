package com.backend.facade.domain;

import com.backend.domain.Currency;
import java.time.LocalDateTime;

public interface StatFacade {
  Long getId();
  Currency getCurFrom();
  Currency getCurTo();
  Double getAmount();
  Double getExRate();
  LocalDateTime getDateTime();
}