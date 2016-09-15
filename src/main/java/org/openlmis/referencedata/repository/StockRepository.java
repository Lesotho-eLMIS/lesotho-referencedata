package org.openlmis.referencedata.repository;

import org.openlmis.referencedata.domain.Stock;
import org.openlmis.referencedata.repository.custom.StockRepositoryCustom;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface StockRepository extends
        PagingAndSortingRepository<Stock, UUID>,
        StockRepositoryCustom {
}
