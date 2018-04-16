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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SearchParamsTest {

  private MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

  @Test
  public void containsKeyShouldReturnTrueIfKeyWasAdded() {
    String someParam = "someParam";
    map.add(someParam, RandomStringUtils.random(5));

    SearchParams searchParams = new SearchParams(map);

    assertTrue(searchParams.containsKey(someParam));
  }

  @Test
  public void containsKeyShouldReturnFalseIfKeyWasNotAdded() {
    SearchParams searchParams = new SearchParams(map);

    assertFalse(searchParams.containsKey("notExist"));
  }

  @Test
  public void shouldRemoveAccessTokenParamWhenCreateObject() {
    String accessToken = "access_token";
    map.add(accessToken, UUID.randomUUID().toString());

    SearchParams searchParams = new SearchParams(map);

    assertFalse(searchParams.containsKey(accessToken));
  }

  @Test
  public void shouldRemovePageParamWhenCreateObject() {
    String page = "page";
    map.add(page, UUID.randomUUID().toString());

    SearchParams searchParams = new SearchParams(map);

    assertFalse(searchParams.containsKey(page));
  }

  @Test
  public void shouldRemoveSizeParamWhenCreateObject() {
    String size = "size";
    map.add(size, UUID.randomUUID().toString());

    SearchParams searchParams = new SearchParams(map);

    assertFalse(searchParams.containsKey(size));
  }

  @Test
  public void shouldRemoveSortParamWhenCreateObject() {
    String sort = "sort";
    map.add(sort, UUID.randomUUID().toString());

    SearchParams searchParams = new SearchParams(map);

    assertFalse(searchParams.containsKey(sort));
  }

}