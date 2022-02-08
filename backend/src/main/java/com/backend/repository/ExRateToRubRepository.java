package com.backend.repository;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExRateToRubRepository extends CrudRepository<ExRateToRub, Long> {
  List<ExRateToRub> findAll();

  @Query(value = "select ex " +
      "from ExRateToRub ex " +
      "where ex.currency = :currency " +
      "and ex.date = :date")
  List<ExRateToRub> findByCurrencyAndDate(@Param("currency") Currency currency,
                                          @Param("date") LocalDate date);

  @Query(nativeQuery = true,
      value = "select * " +
          "from exratetorub ex " +
          "where currency_id = :currency " +
          "and date = " +
          "(select date from exratetorub order by date desc limit 1)")
  List<ExRateToRub> findByCurrencyAndLatestDate(@Param("currency") String currency);

  @Query(nativeQuery = true,
      value = "select * " +
          "from exratetorub ex " +
          "where date = (select date from exratetorub order by date desc limit 1)")
  List<ExRateToRub> findAllByLatestDate();
}