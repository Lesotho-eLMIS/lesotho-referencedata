package org.openlmis.referencedata.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stocks", schema = "referencedata")
@NoArgsConstructor
public class Stock extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "productId", nullable = false)
  @Getter
  @Setter
  private Product product;

  @Getter
  @Setter
  private Long storedQuantity;

  /**
   * Copy values of attributes into new or updated Stock.
   *
   * @param stock Stock with new values.
   */
  public void updateFrom(Stock stock) {
    this.product = stock.getProduct();
    this.storedQuantity = stock.getStoredQuantity();
  }
}
