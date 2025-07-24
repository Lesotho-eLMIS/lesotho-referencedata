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

import java.util.List;
import java.util.UUID;

public class ApprovedProductsRequest {

  private List<UUID> programId;
  private Boolean fullSupply;
  private List<UUID> orderableId;
  private Boolean active;
  private String orderableCode;
  private String orderableName;

  // Getters and setters
  public List<UUID> getProgramId() {
    return programId;
  }

  public void setProgramId(List<UUID> programId) {
    this.programId = programId;
  }

  public Boolean getFullSupply() {
    return fullSupply;
  }

  public void setFullSupply(Boolean fullSupply) {
    this.fullSupply = fullSupply;
  }

  public List<UUID> getOrderableId() {
    return orderableId;
  }

  public void setOrderableId(List<UUID> orderableId) {
    this.orderableId = orderableId;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getOrderableCode() {
    return orderableCode;
  }

  public void setOrderableCode(String orderableCode) {
    this.orderableCode = orderableCode;
  }

  public String getOrderableName() {
    return orderableName;
  }

  public void setOrderableName(String orderableName) {
    this.orderableName = orderableName; 
  }

}
