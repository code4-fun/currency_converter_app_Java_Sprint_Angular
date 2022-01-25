package com.backend.controller;

import com.backend.dto.ConversionResultDto;
import com.backend.dto.CurrenciesDto;
import com.backend.dto.HistoryDto;
import com.backend.dto.StatisticsDto;
import com.backend.service.ConversionService;
import com.backend.service.CurrencyService;
import com.backend.service.DataProcessingService;
import com.backend.service.StatService;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
  @NonNull DataProcessingService dataProcessingService;
  @NonNull CurrencyService currencyService;
  @NonNull ConversionService conversionService;
  @NonNull StatService statService;

  @GetMapping
  public CurrenciesDto startPage() throws IOException {
    dataProcessingService.parse();
    return CurrenciesDto
        .builder()
        .currencies(currencyService.getAllCurrenciesCharCodes())
        .build();
  }

  @GetMapping("/convert")
  public ConversionResultDto convert(@RequestParam("curfrom") String curfrom,
                                     @RequestParam("curto") String curto,
                                     @RequestParam("amount") String amount ){
    return conversionService.convertCurrency(curfrom, curto, Double.parseDouble(amount));
  }

  @GetMapping("/history")
  public List<HistoryDto> getHistory(){
    return statService.getHistory();
  }

  @GetMapping("/stat")
  public List<StatisticsDto> getStatistics(){
    return statService.getStatistics();
  }
}