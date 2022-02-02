package com.backend.controller.dto;

import com.backend.controller.dto.view.CurrencyViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

/**
 *                         __________endpoints__________
 *                        | /convert | /history | /stat |
 *   dateTime             |    +     |    +     |       |
 *   curFrom              |          |    +     |   +   |
 *   curTo                |          |    +     |   +   |
 *   exRate               |    +     |    +     |       |
 *   sumBeforeConversion  |          |    +     |   +   |
 *   sumAfterConversion   |          |    +     |       |
 *   averageExRate        |          |          |   +   |
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
  @JsonView(CurrencyViews.Convert.class)
  private String dateTime;

  @JsonView({
    CurrencyViews.History.class,
    CurrencyViews.Stat.class})
  private String curFrom;

  @JsonView({
    CurrencyViews.History.class,
    CurrencyViews.Stat.class})
  private String curTo;

  @JsonView(CurrencyViews.History.class)
  private String exRate;

  @JsonView({
    CurrencyViews.History.class,
    CurrencyViews.Stat.class})
  private Double sumBeforeConversion;

  @JsonView(CurrencyViews.Convert.class)
  private Double sumAfterConversion;

  @JsonView(CurrencyViews.Stat.class)
  private Double averageExRate;

  @JsonIgnore
  private Double exRateSum;

  @JsonIgnore
  private Integer counter;

  public void addValueToAmountConverted(Double value){
    this.sumBeforeConversion = sumBeforeConversion + value;
  }

  public void addValueToExRateSum(Double value){
    this.exRateSum = exRateSum + value;
  }

  public void incrementCounter(){
    this.counter++;
  }

  public void calculateAverageExRate(){
    averageExRate = Precision.round(exRateSum / counter, 4);
  }
}