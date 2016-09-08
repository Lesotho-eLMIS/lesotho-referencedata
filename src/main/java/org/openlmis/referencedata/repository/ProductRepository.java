package org.openlmis.referencedata.repository;

import org.openlmis.referencedata.domain.Product;

import java.util.UUID;

public interface ProductRepository extends ReferenceDataRepository<Product, UUID> {
  // Add custom Product related members here. See UserRepository.java for examples.

  @Override
  <S extends Product> S save(S entity);

  @Override
  <S extends Product> Iterable<S> save(Iterable<S> entities);
}
