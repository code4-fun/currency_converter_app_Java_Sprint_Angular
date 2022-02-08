package com.backend.controller;

import com.backend.controller.dto.CurrencyDto;
import com.backend.controller.dto.view.CurrencyViews;
import com.backend.facade.service.ConversionServiceFacade;
import com.backend.facade.service.CurrencyServiceFacade;
import com.backend.facade.service.DataProcessingServiceFacade;
import com.backend.facade.service.StatServiceFacade;
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
  @NonNull private final DataProcessingServiceFacade dataProcessingService;
  @NonNull private final CurrencyServiceFacade currencyService;
  @NonNull private final ConversionServiceFacade conversionService;
  @NonNull private final StatServiceFacade statService;

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