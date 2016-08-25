package org.openlmis.referencedata.web;

import org.openlmis.referencedata.i18n.ExposedMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MessageController {

  Logger logger = LoggerFactory.getLogger(ServiceNameController.class);

  @Autowired
  private ExposedMessageSource messageSource;

  /**
   * Example usage of ExposedMessageSource.
   * @return hello world message
   */
  @RequestMapping("/hello")
  public String hello() {
    String[] msgArgs = {"world"};
    logger.debug("Returning hello world message");
    return messageSource.getMessage("referencedata.message.hello", msgArgs, 
        LocaleContextHolder.getLocale());
  }

  @RequestMapping("/messages")
  public Map<String, String> getAllMessages() {
    logger.info("Returning all messages for current locale");
    return messageSource.getAllMessages(LocaleContextHolder.getLocale());
  }
}
