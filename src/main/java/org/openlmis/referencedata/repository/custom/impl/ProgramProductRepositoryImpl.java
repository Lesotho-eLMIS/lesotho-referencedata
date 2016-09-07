package org.openlmis.referencedata.repository.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.openlmis.referencedata.domain.Program;
import org.openlmis.referencedata.domain.ProgramProduct;
import org.openlmis.referencedata.repository.custom.ProgramProductRepositoryCustom;

import java.util.List;

public class ProgramProductRepositoryImpl implements ProgramProductRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Finds ProgramProducts matching all of provided parameters.
   * @param program program of searched ProgramProducts.
   * @return list of all ProgramProducts matching all of provided parameters.
   */
  public List<ProgramProduct> searchProgramProducts(Program program) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProgramProduct> query = builder.createQuery(ProgramProduct.class);
    Root<ProgramProduct> root = query.from(ProgramProduct.class);
    Predicate predicate = builder.conjunction();

    if (program != null) {
      predicate = builder.and(
              predicate,
              builder.equal(
                      root.get("program"), program));
    }
    query.where(predicate);
    return entityManager.createQuery(query).getResultList();
  }
}
