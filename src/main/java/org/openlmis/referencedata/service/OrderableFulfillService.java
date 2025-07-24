/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.referencedata.service;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.openlmis.referencedata.domain.CommodityType;
import org.openlmis.referencedata.domain.FacilityTypeApprovedProduct;
import org.openlmis.referencedata.domain.Orderable;
import org.openlmis.referencedata.domain.TradeItem;
import org.openlmis.referencedata.repository.CommodityTypeRepository;
import org.openlmis.referencedata.repository.FacilityTypeApprovedProductRepository;
import org.openlmis.referencedata.repository.OrderableRepository;
import org.openlmis.referencedata.repository.TradeItemRepository;
import org.openlmis.referencedata.util.EntityCollection;
import org.openlmis.referencedata.util.Pagination;
import org.openlmis.referencedata.web.OrderableFulfill;
import org.openlmis.referencedata.web.OrderableFulfillFactory;
import org.openlmis.referencedata.web.OrderableFulfillSearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderableFulfillService {

  private static final PageRequest NO_PAGINATION = PageRequest.of(Pagination.DEFAULT_PAGE_NUMBER,
      Pagination.NO_PAGINATION);

  @Autowired
  private OrderableRepository orderableRepository;

  @Autowired
  private TradeItemRepository tradeItemRepository;

  @Autowired
  private CommodityTypeRepository commodityTypeRepository;

  @Autowired
  private FacilityTypeApprovedProductRepository ftapRepository;

  @Autowired
  private OrderableFulfillFactory orderableFulfillFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderableFulfillService.class);

  /**
    * Retrieves a map of OrderableFulfill objects based on the provided search parameters.
    * The keys of the map are the UUIDs of the Orderables, and the values are the corresponding
    * OrderableFulfill objects.
    *
    * @param searchParams The search parameters to filter Orderables.
    * @return A map of OrderableFulfill objects keyed by their Orderable UUIDs.
    */
  @Cacheable(value = "orderableFulfills", key = "#searchParams.cacheKey()")
  public Map<UUID, OrderableFulfill> getOrderableFulfills(OrderableFulfillSearchParams searchParams) {
    Set<UUID> ids = getOrderableIds(searchParams);

    Profiler profiler = new Profiler("ORDERABLE_FULFILL_SERVICE_GET_ORDERABLE_FULFILLS");
    profiler.setLogger(LOGGER);
    profiler.start("FETCH_ALL_TRADE_ITEMS_AND_COMMODITY_TYPES");
    EntityCollection<TradeItem> tradeItems = new EntityCollection<>(tradeItemRepository.findAll());
    EntityCollection<CommodityType> commodityTypes = new EntityCollection<>(commodityTypeRepository.findAll());
    profiler.start("FETCH_ALL_ORDERABLES");
    List<Orderable> orderables = getOrderables(ids);
    profiler.start("CREATE_ORDERABLE_FULFILLS");
    Map<UUID, OrderableFulfill> map = Maps.newHashMap();
    orderables.forEach(orderable -> {
      Optional.ofNullable(orderableFulfillFactory.createFor(orderable, tradeItems, commodityTypes))
          .ifPresent(fulfill -> map.put(orderable.getId(), fulfill));
    });
    profiler.stop().log();
    return map;
  }

  private Set<UUID> getOrderableIds(OrderableFulfillSearchParams query) {
    if (query.isSearchByFacilityIdAndProgramId()) {
      return ftapRepository
          .searchProducts(query.getFacilityId(), query.getProgramId(), null, null,
              true, null, null, NO_PAGINATION)
          .getContent()
          .stream()
          .map(FacilityTypeApprovedProduct::getOrderableId)
          .collect(Collectors.toSet());
    }
    return query.getIds();
  }

  private List<Orderable> getOrderables(Set<UUID> ids) {
    Page<Orderable> page = ids.isEmpty()
        ? orderableRepository.findAllLatest(NO_PAGINATION)
        : orderableRepository.findAllLatestByIds(ids, NO_PAGINATION);
    return page.getContent();
  }
}
