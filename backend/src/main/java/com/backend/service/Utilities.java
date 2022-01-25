package com.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class Utilities {
  /**
   * Calculates first day of the current week.
   * @return LocalDateTime object containing the start date and time of the current week
   */
  public static LocalDateTime startOfWeek() {
    return LocalDateTime.now(ZoneId.of("Europe/Moscow"))
        .with(LocalTime.MIN)
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  }

  /**
   * Calculates last day of the current week.
   * @return LocalDateTime object containing the end date and time of the current week
   */
  public static LocalDateTime endOfWeek() {
    return LocalDateTime.now(ZoneId.of("Europe/Moscow"))
        .with(LocalTime.MAX)
        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
  }

  /**
   * Converts string of type 25.01.2022 to the LocalDate instance.
   * @param date string to convert
   * @return LocalDate object
   */
  public static LocalDate convertStringToLocalDate(String date){
    return LocalDate.of(
        Integer.parseInt(date.substring(6)),
        Integer.parseInt(date.substring(3, 5)),
        Integer.parseInt(date.substring(0,2))
    );
  }
}