package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import com.backend.controller.dto.CurrencyDto;
import com.backend.facade.service.ConversionServiceFacade;
import com.backend.facade.service.CurrencyServiceFacade;
import com.backend.facade.service.ExRateToRubServiceFacade;
import com.backend.facade.service.StatServiceFacade;
import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversionService implements ConversionServiceFacade {
  @NonNull private final ExRateToRubServiceFacade exRateToRubService;
  @NonNull private final CurrencyServiceFacade currencyService;
  @NonNull private final StatServiceFacade statService;

  /**
   * Converts given amount of currency curFrom into currency curTo
   * at the latest available exchange rate of the Central Bank.
   * @param curFrom currency to convert from
   * @param curTo currency to convert to
   * @param amount sum or initial currency
   * @return CurrencyDto object containing date of exchange rate and
   * resulting amount of currency after conversion
   */
  @Transactional
  public CurrencyDto convertCurrency(String curFrom, String curTo, Double amount){
    Currency currencyFrom = currencyService.getCurrencyByCharCode(curFrom);
    Currency currencyTo = currencyService.getCurrencyByCharCode(curTo);

    ExRateToRub exRateFrom = exRateToRubService
      .getExRateToRubByCurrencyAndLatestDate(currencyFrom);
    ExRateToRub exRateTo = exRateToRubService
      .getExRateToRubByCurrencyAndLatestDate(currencyTo);

    if(exRateFrom.getDate().equals(exRateTo.getDate())){
      Double rateFrom = exRateFrom.getValue() / exRateFrom.getNominal();
      Double rateTo = exRateTo.getValue() / exRateTo.getNominal();
      Double crossRate = rateFrom / rateTo;
      Double result = crossRate * amount;

      statService.saveStatistics(currencyFrom, currencyTo, amount,
        crossRate, LocalDateTime.now());

      return CurrencyDto
        .builder()
        .dateTime(exRateFrom.getDate().toString())
        .sumAfterConversion(Precision.round(result, 4))
        .build();
    }
    return null;
  }
}