package org.openlmis.referencedata.repository.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.openlmis.referencedata.domain.Program;
import org.openlmis.referencedata.domain.SupervisoryNode;
import org.openlmis.referencedata.domain.SupplyLine;

import java.util.List;

public class SupplyLineRepositoryImpl {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Method returns all Supply lines with matched parameters.
   * @param program program of searched Supply Lines.
   * @param supervisoryNode supervisoryNode of searched Supply Lines.
   * @return list of Supply Lines with matched parameters.
   */
  public List<SupplyLine> searchSupplyLines(Program program, SupervisoryNode supervisoryNode) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SupplyLine> query = builder.createQuery(SupplyLine.class);
    Root<SupplyLine> root = query.from(SupplyLine.class);
    Predicate predicate = builder.conjunction();

    if (program != null) {
      predicate = builder.and(
              predicate,
              builder.equal(
                      root.get("program"), program));
    }
    if (supervisoryNode != null) {
      predicate = builder.and(
              predicate,
              builder.equal(
                      root.get("supervisoryNode"), supervisoryNode));
    }
    query.where(predicate);
    return entityManager.createQuery(query).getResultList();
  }
}
