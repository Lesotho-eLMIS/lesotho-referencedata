package org.openlmis.referencedata.web;

import org.openlmis.referencedata.util.ServiceSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceNameController {

  Logger logger = LoggerFactory.getLogger(ServiceNameController.class);

  @RequestMapping("/")
  public ServiceSignature index() {
    logger.debug("Returning service name and version");
    return new ServiceSignature(ServiceSignature.SERVICE_NAME, ServiceSignature.SERVICE_VERSION);
  }
}