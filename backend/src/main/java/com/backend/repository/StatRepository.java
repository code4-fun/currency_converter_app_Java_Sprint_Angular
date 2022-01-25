package com.backend.repository;

import com.backend.domain.Stat;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface StatRepository extends CrudRepository<Stat, Long> {
  List<Stat> findAll();

  List<Stat> findByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}