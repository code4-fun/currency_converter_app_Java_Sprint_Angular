package com.backend.repository;

import com.backend.domain.Currency;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, String> {
  List<Currency> findAll();
  List<Currency> findByCharCode(String charCode);
}