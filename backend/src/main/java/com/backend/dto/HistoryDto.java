package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {
  private String dateTime;
  private String curFrom;
  private String curTo;
  private String amount;
  private String exRate;
  private String sumConverted;
}