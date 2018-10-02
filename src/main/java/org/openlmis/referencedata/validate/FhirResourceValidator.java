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

package org.openlmis.referencedata.validate;

import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.openlmis.referencedata.domain.FhirResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.Errors;

@AllArgsConstructor
public abstract class FhirResourceValidator<D extends FhirResource, E extends FhirResource>
    implements BaseValidator {
  static final String IS_FHIR_LOCATION_OWNER = "isFhirLocationOwner";
  static final String EXTRA_DATA = "extraData";

  private Class<D> dtoDefinition;

  @Override
  public boolean supports(Class<?> clazz) {
    return Objects.equals(dtoDefinition, clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    D current = dtoDefinition.cast(target);
    verifyResource(current, errors);

    if (errors.hasErrors()) {
      return;
    }

    boolean currentFlag = isFlagSet(current);

    if (null == current.getId()) {
      if (currentFlag && isUserRequest()) {
        rejectValue(errors, EXTRA_DATA, getUnallowedKeyErrorMessage(), IS_FHIR_LOCATION_OWNER);
      }
    } else {
      E existing = getExistingResource(current);
      boolean existingFlag = isFlagSet(existing);

      if (currentFlag != existingFlag && isUserRequest()) {
        rejectValue(errors, EXTRA_DATA, getModifiedKeyErrorMessage(),
            IS_FHIR_LOCATION_OWNER, String.valueOf(currentFlag));
      }

      if (!errors.hasErrors() && currentFlag && isUserRequest()) {
        verifyInvariants(current, existing, errors);
      }
    }
  }

  abstract void verifyResource(D target, Errors errors);

  abstract E getExistingResource(D target);

  abstract void verifyInvariants(D target, E existing, Errors errors);

  abstract String getUnallowedKeyErrorMessage();

  abstract String getModifiedKeyErrorMessage();

  abstract String getInvariantFieldErrorMessage();

  void rejectIfInvariantWasChanged(Errors errors, String field, Object oldValue, Object newValue) {
    rejectIfNotEqual(errors, oldValue, newValue, field, getInvariantFieldErrorMessage());
  }

  private <T extends FhirResource> boolean isFlagSet(T target) {
    String value = Optional
        .ofNullable(target.getExtraData())
        .orElse(Collections.emptyMap())
        .get(IS_FHIR_LOCATION_OWNER);

    if (isBlank(value)) {
      return false;
    }

    return isTrue(toBooleanObject(value));
  }

  private boolean isUserRequest() {
    OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
        .getContext()
        .getAuthentication();

    return !authentication.isClientOnly();
  }
}
