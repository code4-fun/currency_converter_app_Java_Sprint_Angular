package com.backend.domain;

import com.backend.facade.domain.CurrencyFacade;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="currency")
public class Currency implements CurrencyFacade {
  @Id
  private String id;
  private String numCode;
  private String charCode;
  private String name;
}