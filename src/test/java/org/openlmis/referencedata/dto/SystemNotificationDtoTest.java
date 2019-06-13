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

package org.openlmis.referencedata.dto;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.openlmis.referencedata.ToStringTestUtils;
import org.openlmis.referencedata.domain.SystemNotification;
import org.openlmis.referencedata.testbuilder.SystemNotificationDataBuilder;

public class SystemNotificationDtoTest {

  @Test
  public void equalsContract() {
    EqualsVerifier
        .forClass(SystemNotificationDto.class)
        .withRedefinedSuperclass()
        .suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  @Test
  public void shouldImplementToString() {
    SystemNotificationDto dto = new SystemNotificationDto();
    ToStringTestUtils.verify(SystemNotificationDto.class, dto);
  }

  @Test
  public void shouldSetIsDisplayedFlagAsFalseIfSystemNotificationIsInactive() {
    SystemNotification systemNotification = new SystemNotificationDataBuilder()
        .asInactive()
        .build();
    SystemNotificationDto dto = new SystemNotificationDto();
    systemNotification.export(dto);
    assertFalse(dto.isDisplayed());
  }

  @Test
  public void shouldSetIsDisplayedFlagAsFalseIfSystemNotificationExpired() {
    SystemNotification systemNotification = new SystemNotificationDataBuilder()
        .withExpiryDate(ZonedDateTime.now().minusDays(1))
        .build();
    SystemNotificationDto dto = new SystemNotificationDto();
    systemNotification.export(dto);
    assertFalse(dto.isDisplayed());
  }

  @Test
  public void shouldSetIsDisplayedFlagAsTrueIfSystemNotificationIsActiveAndNonExpired() {
    SystemNotification systemNotification = new SystemNotificationDataBuilder()
        .build();
    SystemNotificationDto dto = new SystemNotificationDto();
    systemNotification.export(dto);
    assertTrue(dto.isDisplayed());
  }

  @Test
  public void shouldSetIsDisplayedFlagAsTrueIfSystemNotificationIsActiveAndExpiryDateIsNull() {
    SystemNotification systemNotification = new SystemNotificationDataBuilder()
        .withoutExpiryDate()
        .build();
    SystemNotificationDto dto = new SystemNotificationDto();
    systemNotification.export(dto);
    assertTrue(dto.isDisplayed());
  }
}
