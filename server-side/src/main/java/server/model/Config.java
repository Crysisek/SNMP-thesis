package server.model;

import java.util.Map;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Represents configuration, that will be exposed at rest endpoint.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@Component
public class Config {

  @Value("${timeInterval}")
  private int timeInterval;

  @Value("#{${nameToOidsMap}}")
  private Map<String, String> nameOidPair;
}