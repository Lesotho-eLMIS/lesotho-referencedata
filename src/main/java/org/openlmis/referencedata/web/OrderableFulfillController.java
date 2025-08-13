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

package org.openlmis.referencedata.web;

import static org.openlmis.referencedata.web.OrderableFulfillController.RESOURCE_PATH;

import java.util.Map;
import java.util.UUID;
import org.openlmis.referencedata.service.OrderableFulfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping(RESOURCE_PATH)
public class OrderableFulfillController extends BaseController {


  public static final String RESOURCE_PATH = API_PATH + "/orderableFulfills";

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderableController.class);

  /**
   * The OrderableFulfillService is used to retrieve orderable fulfills based on search parameters.
   */
  // This service encapsulates the logic for fetching and processing orderable fulfills.
  // It interacts with repositories to fetch data and applies business logic to create the
  // appropriate response objects.

  @Autowired
  private OrderableFulfillService orderableFulfillService;

  /**
   * Retrieves a map of OrderableFulfill objects based on the provided search parameters.
   * The keys of the map are the UUIDs of the Orderables, and the values are the corresponding
   * OrderableFulfill objects.
   *
   * @param requestParams The search parameters to filter Orderables.
   * @return A map of OrderableFulfill objects keyed by their Orderable UUIDs.
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Map<UUID, OrderableFulfill> getOrderableFulfills(
      @RequestParam MultiValueMap<String, Object> requestParams) {

    OrderableFulfillSearchParams searchParams = new OrderableFulfillSearchParams(requestParams);
    return orderableFulfillService.getOrderableFulfills(searchParams);
  }

  /**
   * Retrieves a map of OrderableFulfill objects based on the provided query DTO.
   * This method is used to fetch orderable fulfills using a POST request with a body containing
   * the orderable IDs.
   *
   * @param queryDto The DTO containing the collection of orderable IDs to filter Orderables.
   * @return A map of OrderableFulfill objects keyed by their Orderable UUIDs.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Map<UUID, OrderableFulfill> getOrderableFulfillsPost(
      @RequestBody OrderableFulfillQueryDto queryDto) {

    Profiler profiler = new Profiler("getOrderableFulfillsPost");
    profiler.setLogger(LOGGER);
    profiler.start("prepareSearchParams");    
    MultiValueMap<String, Object> params = new org.springframework.util.LinkedMultiValueMap<>();
    if (queryDto != null && queryDto.getOrderableIds() != null) {
      queryDto.getOrderableIds().forEach(id -> params.add("id", id));
    }   

    // Use the existing service method with the provided orderableIds
    OrderableFulfillSearchParams searchParams = new OrderableFulfillSearchParams(params);

    profiler.start("GET_ORDERABLE_FULFILLS"); 
    Map<UUID, OrderableFulfill> orderableMap = orderableFulfillService.getOrderableFulfills(searchParams);
    profiler.stop().log();
    return orderableMap;
  }

}
