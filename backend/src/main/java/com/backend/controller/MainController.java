package com.backend.controller;

import com.backend.controller.dto.CurrencyDto;
import com.backend.controller.dto.view.CurrencyViews;
import com.backend.service.ConversionService;
import com.backend.service.CurrencyService;
import com.backend.service.DataProcessingService;
import com.backend.service.StatService;
import com.fasterxml.jackson.annotation.JsonView;
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
  @NonNull private final DataProcessingService dataProcessingService;
  @NonNull private final CurrencyService currencyService;
  @NonNull private final ConversionService conversionService;
  @NonNull private final StatService statService;

  @GetMapping
  public List<String> startPage() throws IOException {
    dataProcessingService.parse();
    return currencyService.getAllCurrenciesCharCodes();
  }

  @GetMapping("/convert")
  @JsonView(CurrencyViews.Convert.class)
  public CurrencyDto convert(@RequestParam("curfrom") String curfrom,
                             @RequestParam("curto") String curto,
                             @RequestParam("amount") String amount ){
    return conversionService.convertCurrency(curfrom, curto, Double.parseDouble(amount));
  }

  @GetMapping("/history")
  @JsonView(CurrencyViews.History.class)
  public List<CurrencyDto> getHistory(){
    return statService.getHistory();
  }

  @GetMapping("/stat")
  @JsonView(CurrencyViews.Stat.class)
  public List<CurrencyDto> getStatistics(){
    return statService.getStatistics();
  }
}