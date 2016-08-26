package org.openlmis.referencedata.web;

import javax.validation.Valid;
import org.openlmis.referencedata.domain.Facility;
import org.openlmis.referencedata.domain.User;
import org.openlmis.referencedata.repository.UserRepository;
import org.openlmis.referencedata.service.UserService;
import org.openlmis.referencedata.util.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController extends BaseController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Validator validator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(this.validator);
  }

  /**
   * Custom endpoint for creating and updating users.
   */
  @RequestMapping(value = "/users", method = RequestMethod.POST)
  public ResponseEntity<?> save(@RequestBody @Valid User user, BindingResult bindingResult,
                                OAuth2Authentication auth) {
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
    String token = details.getTokenValue();

    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(getErrors(bindingResult), HttpStatus.BAD_REQUEST);
    }

    try {
      userService.save(user, token);

      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (RestClientException ex) {
      ErrorResponse errorResponse =
            new ErrorResponse("An error occurred while saving user", ex.getMessage());
      LOGGER.error(errorResponse.getMessage(), ex);
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Get all users.
   *
   * @return Users.
   */
  @RequestMapping(value = "/users", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getAllUsers() {
    Iterable<User> users = userRepository.findAll();
    if (users == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(users, HttpStatus.OK);
    }
  }

  /**
   * Get chosen user.
   *
   * @param userId UUID of user whose we want to get
   * @return User.
   */
  @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getUser(@PathVariable("id") UUID userId) {
    User user = userRepository.findOne(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(user, HttpStatus.OK);
    }
  }

  /**
   * Allows deleting user.
   *
   * @param userId UUID of user whose we want to delete
   * @return ResponseEntity containing the HTTP Status
   */
  @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
    User user = userRepository.findOne(userId);
    if (user == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } else {
      try {
        userRepository.delete(user);
      } catch (DataIntegrityViolationException ex) {
        ErrorResponse errorResponse =
              new ErrorResponse("User cannot be deleted because of existing dependencies",
                    ex.getMessage());
        LOGGER.error(errorResponse.getMessage(), ex);
        return new ResponseEntity(HttpStatus.CONFLICT);
      }
      return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
  }

  /**
   * Returns all users with matched parameters
   * @param username username of user we want to search.
   * @param firstName firstName of user we want to search.
   * @param lastName lastName of user we want to search.
   * @param homeFacility homeFacility of user we want to search.
   * @param active is the user account active.
   * @param verified is the user account verified.
   * @return ResponseEntity with list of all Users matching
   *         provided parameters and OK httpStatus.
   */
  @RequestMapping(value = "/users/search", method = RequestMethod.GET)
  public ResponseEntity<?> searchUsers(
          @RequestParam(value = "username", required = false) String username,
          @RequestParam(value = "firstName", required = false) String firstName,
          @RequestParam(value = "lastName", required = false) String lastName,
          @RequestParam(value = "homeFacility", required = false) Facility homeFacility,
          @RequestParam(value = "active", required = false) Boolean active,
          @RequestParam(value = "verified", required = false) Boolean verified) {
    List<User> result = userService.searchUsers(username, firstName,
            lastName, homeFacility, active, verified);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private Map<String, String> getErrors(final BindingResult bindingResult) {
    return new HashMap<String, String>() {
      {
        for (FieldError error : bindingResult.getFieldErrors()) {
          put(error.getField(), error.getDefaultMessage());
        }
      }
    };
  }
}