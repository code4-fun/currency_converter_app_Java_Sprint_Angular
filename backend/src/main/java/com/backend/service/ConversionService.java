package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import com.backend.dto.ConversionResultDto;
import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversionService {
  @NonNull private final ExRateToRubService exRateToRubService;
  @NonNull private final CurrencyService currencyService;
  @NonNull private final StatService statService;

  @Transactional
  public ConversionResultDto convertCurrency(String curFrom, String curTo, Double amount){
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
      return ConversionResultDto
          .builder()
          .date(exRateFrom.getDate())
          .amount(Precision.round(result, 4))
          .status("success")
          .build();
    }
    return ConversionResultDto.builder().status("error").build();
  }
}