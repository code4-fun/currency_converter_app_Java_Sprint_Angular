package com.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
  private String curFrom;
  private String curTo;
  private Double amountConverted;
  private Double averageExRate;
  @JsonIgnore
  private Double exRateSum;
  @JsonIgnore
  private Integer counter;

  public void addValueToAmountConverted(Double value){
    this.amountConverted = amountConverted + value;
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